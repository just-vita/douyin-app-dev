package top.vita.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.vita.bo.VlogBO;
import top.vita.enums.YesOrNo;
import top.vita.grace.result.GraceJSONResult;
import top.vita.service.VlogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.vita.utils.PagedGridResult;

/**
 * 短视频表(Vlog)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Api(tags = "视频模块")
@RestController
@RequestMapping("/vlog")
public class VlogController{

    @Autowired
    private VlogService vlogService;

    @ApiOperation("保存上传视频信息接口")
    @PostMapping("/publish")
    public GraceJSONResult publish(@RequestBody VlogBO vlogBO) {
        vlogService.createVlog(vlogBO);
        return GraceJSONResult.ok();
    }

    @ApiOperation("首页视频列表接口")
    @GetMapping("/indexList")
    public GraceJSONResult indexList(@RequestParam String userId,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult indexVlogList = vlogService.getIndexVlogList(userId, search, page, pageSize);
        return GraceJSONResult.ok(indexVlogList);
    }

    @ApiOperation("视频详情接口")
    @GetMapping("/detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                  @RequestParam String vlogId) {
        return GraceJSONResult.ok(vlogService.getVlogDetailById(vlogId));
    }

    @ApiOperation("修改视频为私密接口")
    @PostMapping("/changeToPrivate")
    public GraceJSONResult changeToPrivate(@RequestParam String userId,
                                           @RequestParam String vlogId) {
        vlogService.changeToPublicOrPrivate(userId, vlogId, YesOrNo.YES.type);
        return GraceJSONResult.ok();
    }

    @ApiOperation("修改视频为公开接口")
    @PostMapping("/changeToPublic")
    public GraceJSONResult changeToPublic(@RequestParam String userId,
                                          @RequestParam String vlogId) {
        vlogService.changeToPublicOrPrivate(userId, vlogId, YesOrNo.NO.type);
        return GraceJSONResult.ok();
    }

    @ApiOperation("个人私密视频列表接口")
    @GetMapping("/myPrivateList")
    public GraceJSONResult myPrivateList(@RequestParam String userId,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult myPrivateList = vlogService.getMyVlogList(userId,
                                                                page,
                                                                pageSize,
                                                                YesOrNo.YES.type);
        return GraceJSONResult.ok(myPrivateList);
    }

    @ApiOperation("个人公开视频列表接口")
    @GetMapping("/myPublicList")
    public GraceJSONResult myPublicList(@RequestParam String userId,
                                         @RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult myPublicList = vlogService.getMyVlogList(userId,
                                                                page,
                                                                pageSize,
                                                                YesOrNo.NO.type);
        return GraceJSONResult.ok(myPublicList);
    }

    @ApiOperation("点赞接口")
    @PostMapping("/like")
    public GraceJSONResult like(@RequestParam String userId,
                                @RequestParam String vlogerId,
                                @RequestParam String vlogId) {
        vlogService.like(userId, vlogerId, vlogId);
        return GraceJSONResult.ok();
    }

    @ApiOperation("取消点赞接口")
    @PostMapping("/unlike")
    public GraceJSONResult unlike(@RequestParam String userId,
                                  @RequestParam String vlogerId,
                                  @RequestParam String vlogId) {
        vlogService.unlike(userId, vlogerId, vlogId);
        return GraceJSONResult.ok();
    }

    @ApiOperation("查询点赞数量接口")
    @PostMapping("/totalLikedCounts")
    public GraceJSONResult totalLikedCounts(@RequestParam String vlogId) {
        Integer likedCounts = vlogService.getVlogBeLikedCounts(vlogId);
        return GraceJSONResult.ok(likedCounts);
    }

    @ApiOperation("已点赞视频列表接口")
    @GetMapping("/myLikedList")
    public GraceJSONResult myLikedList(@RequestParam String userId,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult myLikedList =
                vlogService.getMyLikedVlogList(userId, page, pageSize);
        return GraceJSONResult.ok(myLikedList);
    }

    @ApiOperation("已关注博主视频列表接口")
    @GetMapping("/followList")
    public GraceJSONResult followList(@RequestParam String myId,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult result =
                vlogService.getMyFollowVlogList(myId, page, pageSize);
        return GraceJSONResult.ok(result);
    }
}
