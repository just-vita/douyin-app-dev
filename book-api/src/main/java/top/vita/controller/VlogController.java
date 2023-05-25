package top.vita.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;
import top.vita.bo.VlogBO;
import top.vita.grace.result.GraceJSONResult;
import top.vita.service.VlogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.vita.utils.MinIOUtils;
import top.vita.vo.IndexVlogVO;

import java.util.List;

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

    @ApiOperation("获取首页视频列表接口")
    @GetMapping("/indexList")
    public GraceJSONResult indexList(@RequestParam(defaultValue = "") String search) {
        List<IndexVlogVO> indexVlogList = vlogService.getIndexVlogList(search);
        return GraceJSONResult.ok(indexVlogList);
    }
}
