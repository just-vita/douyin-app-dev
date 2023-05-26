package top.vita.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.vita.bo.CommentBO;
import top.vita.bo.VlogBO;
import top.vita.grace.result.GraceJSONResult;
import top.vita.service.CommentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.vita.utils.PagedGridResult;
import top.vita.vo.CommentVO;

import javax.validation.Valid;

/**
 * 评论表(Comment)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:32
 */
@Api(tags = "评论模块")
@RestController
@RequestMapping("/comment")
public class CommentController{

    @Autowired
    private CommentService commentService;

    @ApiOperation("发布评论接口")
    @PostMapping("/create")
    public GraceJSONResult createComment(@RequestBody @Valid CommentBO commentBO) {
        CommentVO commentVO = commentService.createComment(commentBO);
        return GraceJSONResult.ok(commentVO);
    }

    @ApiOperation("评论数量接口")
    @GetMapping("/counts")
    public GraceJSONResult counts(@RequestParam String vlogId) {
        Integer count = commentService.getVlogCommentCountFromRedis(vlogId);
        return GraceJSONResult.ok(count);
    }

    @ApiOperation("评论列表接口")
    @GetMapping("/list")
    public GraceJSONResult list(@RequestParam String vlogId,
                                @RequestParam(defaultValue = "") String userId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult result =
                commentService.getVlogCommentList(vlogId, userId, page, pageSize);
        return GraceJSONResult.ok(result);
    }
}
