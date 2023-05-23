package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.vita.pojo.Fans;
import top.vita.mapper.FansMapper;
import top.vita.service.FansService;

/**
 * 粉丝表

(Fans)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Service("fansService")
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements FansService {

}

