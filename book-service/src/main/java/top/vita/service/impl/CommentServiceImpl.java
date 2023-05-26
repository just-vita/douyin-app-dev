package top.vita.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.bo.CommentBO;
import top.vita.pojo.Comment;
import top.vita.mapper.CommentMapper;
import top.vita.service.CommentService;
import top.vita.utils.PagedGridResult;
import top.vita.utils.RedisOperator;
import top.vita.vo.CommentVO;
import top.vita.vo.IndexVlogVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.vita.service.base.BaseInfoProperties.*;

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

    @Override
    public CommentVO createComment(CommentBO commentBO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentBO, comment);
        comment.setId(sid.nextShort());
        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());
        save(comment);

        // 自增视频评论数
        redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + comment.getVlogId(), 1);
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
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
        return setterPagedGrid(list, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(String commentUserId, String commentId, String vlogId) {
        lambdaUpdate()
                .eq(Comment::getCommentUserId, commentUserId)
                .eq(Comment::getId, commentId)
                .remove();
        // 减少redis中存放的评论数量
        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);
    }

    @Override
    public void likeComment(String commentId, String userId) {
        redis.increment(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + commentId, 1);
        redis.set(REDIS_USER_LIKE_COMMENT + ":" + userId, "1");
    }

    @Override
    public void unlikeComment(String commentId, String userId) {
        redis.decrement(REDIS_VLOG_COMMENT_LIKED_COUNTS + ":" + commentId, 1);
        redis.del(REDIS_USER_LIKE_COMMENT + ":" + userId);
    }
}

