package top.vita.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.vita.grace.result.GraceJSONResult;
import top.vita.grace.utils.IPUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author vita
 * @Date 2023/5/23 23:39
 */
@Api(tags = "短信验证接口")
@RestController
public class PassportController extends BaseInfoProperties {

    @ApiOperation("短信验证接口")
    @PostMapping("/passport/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                                      HttpServletRequest request){
        if (StringUtils.isBlank(mobile)){
            return GraceJSONResult.ok();
        }

        String userIp = IPUtil.getRequestIp(request);
        // 使用redis限制60秒内只能获取一次，只存储占位符
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        // 生成6位验证码
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        // 验证码五分钟过期
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 5 * 60);
        System.out.println(code);
        return GraceJSONResult.ok();
    }
}
