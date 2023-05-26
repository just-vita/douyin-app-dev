package top.vita.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.vita.config.MinIOConfig;
import top.vita.grace.result.GraceJSONResult;
import top.vita.mo.MessageMO;
import top.vita.service.MsgService;
import top.vita.service.base.BaseInfoProperties;
import top.vita.utils.MinIOUtils;
import top.vita.utils.PagedGridResult;

import java.util.List;

/**
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Api(tags = "消息模块")
@RestController
@RequestMapping("/msg")
public class MsgController extends BaseInfoProperties {

    @Autowired
    private MsgService msgService;

    @ApiOperation("消息列表接口")
    @GetMapping("/list")
    public GraceJSONResult list(@RequestParam String userId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        List<MessageMO> result =
                msgService.queryList(userId, page, pageSize);
        return GraceJSONResult.ok(result);
    }
}
