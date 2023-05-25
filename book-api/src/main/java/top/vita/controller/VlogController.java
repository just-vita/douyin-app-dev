package top.vita.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.vita.bo.VlogBO;
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
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PagedGridResult indexVlogList = vlogService.getIndexVlogList(search, page, pageSize);
        return GraceJSONResult.ok(indexVlogList);
    }

    @ApiOperation("视频详情接口")
    @GetMapping("/detail")
    public GraceJSONResult detail(@RequestParam(defaultValue = "") String userId,
                                     @RequestParam String vlogId) {
        return GraceJSONResult.ok(vlogService.getVlogDetailById(vlogId));
    }
}
