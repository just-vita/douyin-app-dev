package top.vita.controller;



import top.vita.service.CommentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 评论表(Comment)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:32
 */
@RestController
@RequestMapping("/comment")
public class CommentController{

    @Autowired
    private CommentService commentService;

}
