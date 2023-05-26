package top.vita.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.bo.CommentBO;
import top.vita.pojo.Comment;
import top.vita.utils.PagedGridResult;
import top.vita.vo.CommentVO;

/**
 * 评论表(Comment)表服务接口
 *
 * @author vita
 * @since 2023-05-24 00:57:34
 */
public interface CommentService extends IService<Comment> {

    CommentVO createComment(CommentBO commentBO);

    Integer getVlogCommentCountFromRedis(String vlogId);

    PagedGridResult getVlogCommentList(String vlogId, String userId, Integer page, Integer pageSize);

    void deleteComment(String commentUserId, String commentId, String vlogId);

    void likeComment(String commentId, String userId);

    void unlikeComment(String commentId, String userId);
}

