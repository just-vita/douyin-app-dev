package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.vita.pojo.Vlog;
import top.vita.mapper.VlogMapper;
import top.vita.service.VlogService;

/**
 * 短视频表(Vlog)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Service("vlogService")
public class VlogServiceImpl extends ServiceImpl<VlogMapper, Vlog> implements VlogService {

}

