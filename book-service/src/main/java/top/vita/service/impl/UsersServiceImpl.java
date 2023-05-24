package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vita.enums.Sex;
import top.vita.enums.YesOrNo;
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

    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";

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
}

