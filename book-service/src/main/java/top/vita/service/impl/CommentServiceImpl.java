package top.vita.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vita.bo.CommentBO;
import top.vita.pojo.Comment;
import top.vita.mapper.CommentMapper;
import top.vita.service.CommentService;
import top.vita.utils.RedisOperator;
import top.vita.vo.CommentVO;

import java.util.Date;

import static top.vita.service.base.BaseInfoProperties.REDIS_VLOG_COMMENT_COUNTS;

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
}

