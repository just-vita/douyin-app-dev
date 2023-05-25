package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vita.bo.UpdatedUserBO;
import top.vita.enums.Sex;
import top.vita.enums.UserInfoModifyType;
import top.vita.enums.YesOrNo;
import top.vita.exceptions.GraceException;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.pojo.Users;
import top.vita.mapper.UsersMapper;
import top.vita.service.UsersService;
import top.vita.utils.DateUtil;
import top.vita.utils.DesensitizationUtil;

import java.util.Date;

/**
 * 用户表(Users)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private Sid sid;

    private static final String USER_FACE1 = "https://learn-1312191491.cos.ap-beijing.myqcloud.com/img%2F%E4%B8%AA%E4%BA%BA%E5%8D%9A%E5%AE%A2%2F%E5%A4%B4%E5%83%8F%2F9.jpg";

    @Override
    public Users queryUserIsExist(String mobile) {
        Users users = lambdaQuery()
                .eq(Users::getMobile, mobile)
                .one();
        return users;
    }

    @Override
    public Users createUser(String mobile) {
        // 获得全局唯一主键
        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);

        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setImoocNum("用户：" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE1);

        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);

        user.setCountry("中国");
        user.setProvince("");
        user.setCity("");
        user.setDistrict("");
        user.setDescription("这家伙很懒，什么都没留下~");
        user.setCanImoocNumBeUpdated(YesOrNo.YES.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        save(user);

        return user;
    }

    @Override
    public Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type) {
        if (UserInfoModifyType.NICKNAME.type.equals(type)) {
            // 修改名称时要判断是否有重复
            Integer count = lambdaQuery()
                    .eq(Users::getNickname, updatedUserBO.getNickname())
                    .count();
            if (count != 0) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
        } else if (UserInfoModifyType.IMOOCNUM.type.equals(type)) {
            // 修改账户号码时要判断是否有重复
            Integer count = lambdaQuery()
                    .eq(Users::getImoocNum, updatedUserBO.getImoocNum())
                    .count();
            if (count != 0) {
                GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_NICKNAME_EXIST_ERROR);
            }
            // 如果不可修改，就返回错误信息
            Users tempUser =  getById(updatedUserBO.getId());
            if (YesOrNo.NO.type.equals(tempUser.getCanImoocNumBeUpdated())) {
                GraceException.display(ResponseStatusEnum.USER_INFO_CANT_UPDATED_IMOOCNUM_ERROR);
            }
            // 如果可以修改，那么修改完这次就不能再修改了
            updatedUserBO.setCanImoocNumBeUpdated(YesOrNo.NO.type);
        }
        return updateUserInfo(updatedUserBO);
    }

    @Override
    public boolean checkTwoUserExists(String userId1, String userId2) {
        return lambdaQuery()
                .eq(Users::getId, userId1)
                .or()
                .eq(Users::getId, userId2)
                .count() == 2;
    }

    public Users updateUserInfo(UpdatedUserBO updatedUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(updatedUserBO, users);
        boolean flag = updateById(users);
        if (!flag) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        return getById(updatedUserBO.getId());
    }
}

