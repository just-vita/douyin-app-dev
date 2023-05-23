package top.vita.controller;



import top.vita.service.MyLikedVlogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 点赞短视频关联表
(MyLikedVlog)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@RestController
@RequestMapping("/myLikedVlog")
public class MyLikedVlogController{

    @Autowired
    private MyLikedVlogService myLikedVlogService;

}
