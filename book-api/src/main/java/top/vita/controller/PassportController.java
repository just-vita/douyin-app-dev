package top.vita.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.vita.bo.RegistLoginBO;
import top.vita.grace.result.GraceJSONResult;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.pojo.Users;
import top.vita.service.impl.UsersServiceImpl;
import top.vita.utils.IPUtil;
import top.vita.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * @Author vita
 * @Date 2023/5/23 23:39
 */
@Api(tags = "通行证")
@RestController
@RequestMapping("/passport")
public class PassportController extends BaseInfoProperties {

    @Autowired
    private UsersServiceImpl usersService;

    @ApiOperation("短信验证接口")
    @PostMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile,
                                      HttpServletRequest request) {
        if (StringUtils.isBlank(mobile)) {
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

    @ApiOperation("登录验证接口")
    @PostMapping("/login")
    public GraceJSONResult login(@Valid @RequestBody RegistLoginBO registLoginBO) {
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        // 判断验证码是否正确
        String code = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 判断用户是否存在，不存在则创建
        Users user = usersService.queryUserIsExist(mobile);
        if (user == null) {
            user = usersService.createUser(mobile);
        }
        // 生成token
        String userToken = UUID.randomUUID().toString();
        // 缓存token
        redis.set(REDIS_USER_TOKEN + ":" + user.getId(), userToken);
        // 登录成功后删除短信验证码的缓存
        redis.del(MOBILE_SMSCODE + ":" + mobile);
        // 构建返回对象
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setUserToken(userToken);
        return GraceJSONResult.ok(userVo);
    }

    @ApiOperation("退出接口")
    @PostMapping("/logout")
    public GraceJSONResult login(@RequestParam String userId) {
        redis.del(REDIS_USER_TOKEN + ":" + userId);
        return GraceJSONResult.ok();
    }
}
