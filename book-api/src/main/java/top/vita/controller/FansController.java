package top.vita.controller;



import top.vita.service.FansService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 粉丝表

(Fans)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@RestController
@RequestMapping("/fans")
public class FansController{

    @Autowired
    private FansService fansService;

}
