package top.vita.controller;



import top.vita.service.UsersService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户表(Users)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@RestController
@RequestMapping("/users")
public class UsersController{

    @Autowired
    private UsersService usersService;

}
