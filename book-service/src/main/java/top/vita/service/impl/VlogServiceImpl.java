package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
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
import top.vita.service.base.BaseInfoProperties;
import top.vita.utils.PagedGridResult;
import top.vita.vo.IndexVlogVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.vita.service.base.BaseInfoProperties.setterPagedGrid;

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
    public PagedGridResult getIndexVlogList(String search,
                                            Integer page,
                                            Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)){
            map.put("search", search);
        }
        List<IndexVlogVO> list = vlogMapper.getIndexVlogList(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public Object getVlogDetailById(String vlogId) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);
        // TODO 可优化
        List<IndexVlogVO> list = vlogMapper.getVlogDetailById(map);
        if (list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void changeToPublicOrPrivate(String userId,
                                        String vlogId,
                                        Integer type) {
        lambdaUpdate()
                .eq(Vlog::getId, vlogId)
                .eq(Vlog::getVlogerId, userId)
                .set(Vlog::getIsPrivate, type)
                .update();
    }

    @Override
    public PagedGridResult getMyVlogList(String userId,
                                         Integer page,
                                         Integer pageSize,
                                         Integer type) {
        PageHelper.startPage(page, pageSize);
        List<Vlog> list = lambdaQuery()
                .eq(Vlog::getVlogerId, userId)
                .eq(Vlog::getIsPrivate, type)
                .list();
        return setterPagedGrid(list, page);
    }
}

