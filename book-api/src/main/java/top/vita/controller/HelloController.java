package top.vita.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vita.grace.result.GraceJSONResult;

/**
 * @Author vita
 * @Date 2023/5/23 23:39
 */
@Api(tags = "Hello Test")
@RestController
public class HelloController {
    @ApiOperation("hello 接口")
    @GetMapping("/hello")
    public Object hello(){
        return GraceJSONResult.ok("hello");
    }
}
