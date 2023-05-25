package top.vita.controller;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;
import top.vita.service.base.BaseInfoProperties;
import top.vita.bo.UpdatedUserBO;
import top.vita.config.MinIOConfig;
import top.vita.enums.FileTypeEnum;
import top.vita.enums.UserInfoModifyType;
import top.vita.grace.result.GraceJSONResult;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.pojo.Users;
import top.vita.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.vita.utils.MinIOUtils;
import top.vita.vo.UsersVo;

/**
 * 用户表(Users)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Api(tags = "用户信息模块")
@RestController
@RequestMapping("/userInfo")
public class UsersController extends BaseInfoProperties {

    @Autowired
    private UsersService usersService;
    @Autowired
    private MinIOConfig minIOConfig;

    @ApiOperation("获取用户信息接口")
    @GetMapping("/query")
    public GraceJSONResult query(@RequestParam String userId) {
        Users user = usersService.getById(userId);
        UsersVo usersVO = new UsersVo();
        BeanUtils.copyProperties(user, usersVO);

        // 我的关注博主总数量
        String myFollowsCountsStr = redis.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);
        // 我的粉丝总数
        String myFansCountsStr = redis.get(REDIS_MY_FANS_COUNTS + ":" + userId);
        // 用户获赞总数，视频博主（点赞/喜欢）总和
        String likedVlogCountsStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String likedVlogerCountsStr = redis.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.valueOf(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.valueOf(myFansCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
            likedVlogCounts = Integer.valueOf(likedVlogCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.valueOf(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        usersVO.setMyFollowsCounts(myFollowsCounts);
        usersVO.setMyFansCounts(myFansCounts);
        usersVO.setTotalLikeMeCounts(totalLikeMeCounts);
        return GraceJSONResult.ok(usersVO);
    }

    @ApiOperation("修改用户信息接口")
    @PostMapping("/modifyUserInfo")
    public GraceJSONResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                          @RequestParam Integer type) {
        UserInfoModifyType.checkUserInfoTypeIsRight(type);
        Users user = usersService.updateUserInfo(updatedUserBO, type);
        return GraceJSONResult.ok(user);
    }

    @ApiOperation("修改用户头像/背景接口")
    @PostMapping("/modifyImage")
    public GraceJSONResult modifyImage(@RequestParam String userId,
                                       @RequestParam Integer type,
                                       MultipartFile file) throws Exception {
        // 类型有问题
        if (FileTypeEnum.BGIMG.type.equals(type) && FileTypeEnum.FACE.type.equals(type)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();
        // 上传文件
        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                filename,
                file.getInputStream());
        // 拼接url
        String imgUrl = minIOConfig.getFileHost() + "/" +
                minIOConfig.getBucketName() + "/" +
                filename;
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);
        if (FileTypeEnum.BGIMG.type.equals(type)){
            updatedUserBO.setBgImg(imgUrl);
        } else {
            updatedUserBO.setFace(imgUrl);
        }
        Users users = usersService.updateUserInfo(updatedUserBO);
        return GraceJSONResult.ok(users);
    }



}
