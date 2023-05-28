package top.vita.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.bo.CommentBO;
import top.vita.enums.MessageEnum;
import top.vita.mo.MessageContent;
import top.vita.mo.MessageMO;
import top.vita.pojo.Comment;
import top.vita.mapper.CommentMapper;
import top.vita.pojo.Vlog;
import top.vita.service.CommentService;
import top.vita.service.MsgService;
import top.vita.service.VlogService;
import top.vita.service.base.RabbitMQConfig;
import top.vita.utils.JsonUtils;
import top.vita.utils.PagedGridResult;
import top.vita.utils.RedisOperator;
import top.vita.vo.CommentVO;

import java.util.*;

import static top.vita.service.base.BaseInfoProperties.*;
import static top.vita.service.base.RabbitMQConfig.ADD_MSG_ROUTE;
import static top.vita.service.base.RabbitMQConfig.DEL_MSG_ROUTE;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private Sid sid;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MsgService msgService;
    @Autowired
    private VlogService vlogService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public CommentVO createComment(CommentBO commentBO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentBO, comment);
        String commentId = sid.nextShort();
        comment.setId(commentId);
        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());
        save(comment);

        // 自增视频评论数
        redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + comment.getVlogId(), 1);
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);

        // 发送消息给被评论的博主
        String cover = vlogService.getCoverById(commentVO.getVlogId());
        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(commentVO.getFatherCommentId()) &&
                !commentVO.getFatherCommentId().equals("0")){
            type = MessageEnum.REPLY_YOU.type;
        }
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(commentVO.getCommentUserId());
        messageMO.setToUserId(commentVO.getVlogerId());
        messageMO.setMsgType(type);
        messageMO.setVlogId(commentVO.getVlogId());
        messageMO.setVlogCover(cover);
        messageMO.setCommentId(commentId);
        messageMO.setCommentContent(commentVO.getContent());
        messageMO.setMsgType(MessageEnum.COMMENT_VLOG.type);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_MSG,
                ADD_MSG_ROUTE,
                Objects.requireNonNull(JsonUtils.objectToJson(messageMO))
        );
        return commentVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(String commentUserId,
                              String commentId,
                              String vlogId) {
        // 减少redis中存放的评论数量
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);

        // 清除消息
        String vlogerId = vlogService.lambdaQuery()
                         .eq(Vlog::getId, vlogId)
                         .select(Vlog::getVlogerId)
                         .one()
                         .getVlogerId();
        String fatherCommentId = lambdaQuery()
                                .eq(Comment::getId, commentId)
                                .select(Comment::getFatherCommentId)
                                .one()
                                .getFatherCommentId();
        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(fatherCommentId) &&
                !fatherCommentId.equals("0")){
            type = MessageEnum.REPLY_YOU.type;
        }

        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(commentUserId);
        messageMO.setToUserId(vlogerId);
        messageMO.setCommentId(commentId);
        messageMO.setMsgType(type);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_MSG,
                DEL_MSG_ROUTE,
                Objects.requireNonNull(JsonUtils.objectToJson(messageMO))
        );

        lambdaUpdate()
                .eq(Comment::getCommentUserId, commentUserId)
                .eq(Comment::getId, commentId)
                .remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(String commentId, String userId) {
        redis.increment(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + commentId, 1);
        redis.set(REDIS_USER_LIKE_COMMENT + ":" + userId + ":" + commentId, "1");

        // 发送点赞消息
        Comment comment = lambdaQuery()
                .eq(Comment::getId, commentId)
                .select(Comment::getVlogId, Comment::getCommentUserId)
                .one();
        String cover = vlogService.getCoverById(comment.getVlogId());
        MessageMO messageMO = new MessageMO();
        messageMO.setVlogId(comment.getVlogId());
        messageMO.setVlogCover(cover);
        messageMO.setCommentId(commentId);
        messageMO.setFromUserId(userId);
        messageMO.setToUserId(comment.getCommentUserId());
        messageMO.setMsgType(MessageEnum.LIKE_COMMENT.type);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_MSG,
                ADD_MSG_ROUTE,
                Objects.requireNonNull(JsonUtils.objectToJson(messageMO))
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlikeComment(String commentId, String userId) {
        redis.decrement(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + commentId, 1);
        redis.del(REDIS_USER_LIKE_COMMENT + ":" + userId + ":" + commentId);

        // 清除点赞消息
        Comment comment = lambdaQuery()
                .eq(Comment::getId, commentId)
                .select(Comment::getCommentUserId)
                .one();
        // 发送消息给消息队列
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(userId);
        messageMO.setToUserId(comment.getCommentUserId());
        messageMO.setCommentId(commentId);
        messageMO.setMsgType(MessageEnum.LIKE_COMMENT.type);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_MSG,
                DEL_MSG_ROUTE,
                Objects.requireNonNull(JsonUtils.objectToJson(messageMO))
        );

    }


    @Override
    public Integer getVlogCommentCountFromRedis(String vlogId) {
        String countStr = redis.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countStr)){
            return 0;
        }
        return Integer.valueOf(countStr);
    }

    @Override
    public PagedGridResult getVlogCommentList(String vlogId, String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        List<CommentVO> list = commentMapper.getCommentList(map);
        for (CommentVO commentVO : list) {
            commentVO.setIsLike(isLikedComment(userId, commentVO.getCommentId()));
            commentVO.setLikeCounts(getLikedComment(commentVO.getCommentId()));
        }
        return setterPagedGrid(list, page);
    }

    private Integer isLikedComment(String userId, String commentId) {
        String isLiked = redis.get(REDIS_USER_LIKE_COMMENT + ":" + userId + ":" + commentId);
        if (!"1".equals(isLiked)){
            return 0;
        }
        return 1;
    }

    private Integer getLikedComment(String commentId) {
        String count = redis.get(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + commentId);
        if (StringUtils.isBlank(count)){
            return 0;
        }
        return Integer.parseInt(count);
    }
}

