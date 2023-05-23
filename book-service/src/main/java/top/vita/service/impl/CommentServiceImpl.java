package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.vita.pojo.Comment;
import top.vita.mapper.CommentMapper;
import top.vita.service.CommentService;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

