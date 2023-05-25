package top.vita.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import top.vita.grace.result.GraceJSONResult;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.service.FansService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.vita.service.UsersService;

/**
 * 粉丝表

(Fans)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Api(tags = "粉丝模块")
@RestController
@RequestMapping("/fans")
public class FansController{

    @Autowired
    private FansService fansService;
    @Autowired
    private UsersService usersService;

    @ApiOperation("关注用户接口")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String myId,
                                  @RequestParam String vlogerId){
        if (StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.PARAMS_ERROR);
        }
        if (myId.equals(vlogerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        boolean flag = usersService.checkTwoUserExists(myId, vlogerId);
        if (!flag){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        fansService.doFollow(myId, vlogerId);
        return GraceJSONResult.ok();
    }

}
