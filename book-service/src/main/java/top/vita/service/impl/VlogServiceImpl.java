package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vita.bo.VlogBO;
import top.vita.enums.YesOrNo;
import top.vita.pojo.Vlog;
import top.vita.mapper.VlogMapper;
import top.vita.service.VlogService;
import top.vita.vo.IndexVlogVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短视频表(Vlog)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:36
 */
@Service("vlogService")
public class VlogServiceImpl extends ServiceImpl<VlogMapper, Vlog> implements VlogService {

    @Autowired
    private Sid sid;
    @Autowired
    private VlogMapper vlogMapper;

    @Override
    public void createVlog(VlogBO vlogBO) {
        String vid = sid.nextShort();

        Vlog vlog = new Vlog();
        BeanUtils.copyProperties(vlogBO, vlog);

        vlog.setId(vid);

        vlog.setLikeCounts(0);
        vlog.setCommentsCounts(0);
        vlog.setIsPrivate(YesOrNo.NO.type);

        vlog.setCreatedTime(new Date());
        vlog.setUpdatedTime(new Date());

        save(vlog);
    }

    @Override
    public List<IndexVlogVO> getIndexVlogList(String search) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)){
            map.put("search", search);
        }
        return vlogMapper.getIndexVlogList(map);
    }
}

