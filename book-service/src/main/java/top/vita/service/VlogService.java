package top.vita.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.vita.bo.VlogBO;
import top.vita.pojo.Vlog;
import top.vita.vo.IndexVlogVO;

import java.util.List;

/**
 * 短视频表(Vlog)表服务接口
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
public interface VlogService extends IService<Vlog> {

    void createVlog(VlogBO vlogBO);

    List<IndexVlogVO> getIndexVlogList(String search);
}

