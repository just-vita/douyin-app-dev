package top.vita.controller;



import top.vita.service.VlogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 短视频表(Vlog)表控制层
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@RestController
@RequestMapping("/vlog")
public class VlogController{

    @Autowired
    private VlogService vlogService;

}
