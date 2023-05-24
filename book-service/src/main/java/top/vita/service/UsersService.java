package top.vita.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.bo.UpdatedUserBO;
import top.vita.pojo.Users;

/**
 * 用户表(Users)表服务接口
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
public interface UsersService extends IService<Users> {

    /**
     * 判断用户是否存在
     */
    Users queryUserIsExist(String mobile);

    /**
     * 创建用户
     */
    Users createUser(String mobile);

    /**
     * 修改用户信息
     */
    Users updateUserInfo(UpdatedUserBO updatedUserBO, Integer type);
}

