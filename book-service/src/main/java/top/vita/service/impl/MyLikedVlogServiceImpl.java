package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.vita.pojo.MyLikedVlog;
import top.vita.mapper.MyLikedVlogMapper;
import top.vita.service.MyLikedVlogService;

/**
 * 点赞短视频关联表
(MyLikedVlog)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Service("myLikedVlogService")
public class MyLikedVlogServiceImpl extends ServiceImpl<MyLikedVlogMapper, MyLikedVlog> implements MyLikedVlogService {

}

