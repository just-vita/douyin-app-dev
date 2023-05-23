package top.vita.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vita.grace.result.GraceJSONResult;

/**
 * @Author vita
 * @Date 2023/5/23 23:39
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello(){
        return GraceJSONResult.ok("hello");
    }
}
