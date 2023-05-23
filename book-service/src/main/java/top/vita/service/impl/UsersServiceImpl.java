package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.vita.pojo.Users;
import top.vita.mapper.UsersMapper;
import top.vita.service.UsersService;

/**
 * 用户表(Users)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}

