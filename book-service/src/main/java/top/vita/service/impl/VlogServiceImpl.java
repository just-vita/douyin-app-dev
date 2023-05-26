package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.bo.VlogBO;
import top.vita.enums.YesOrNo;
import top.vita.pojo.MyLikedVlog;
import top.vita.pojo.Vlog;
import top.vita.mapper.VlogMapper;
import top.vita.service.MyLikedVlogService;
import top.vita.service.VlogService;
import top.vita.utils.PagedGridResult;
import top.vita.utils.RedisOperator;
import top.vita.vo.IndexVlogVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.vita.service.base.BaseInfoProperties.*;

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
    @Autowired
    private MyLikedVlogService myLikedVlogService;
    @Autowired
    private RedisOperator redis;

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
    public PagedGridResult getIndexVlogList(String userId,
                                            String search,
                                            Integer page,
                                            Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(search)){
            map.put("search", search);
        }
        List<IndexVlogVO> list = vlogMapper.getIndexVlogList(map);
        // 设置是否已经点赞了这个视频
        for (IndexVlogVO vlogVO : list) {
            boolean isLiked = isLikedVlog(userId, vlogVO.getVlogId());
            vlogVO.setDoILikeThisVlog(isLiked);
            // 从redis查询视频点赞总数
            Integer count = getVlogBeLikedCounts(vlogVO.getVlogId());
            vlogVO.setLikeCounts(count);
        }
        return setterPagedGrid(list, page);
    }

    @Override
    public Integer getVlogBeLikedCounts(String vlogId) {
        String countStr = redis.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countStr)){
            return 0;
        }
        return Integer.valueOf(countStr);
    }

    @Override
    public PagedGridResult getMyLikedVlogList(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<IndexVlogVO> list = vlogMapper.getMyLikedVlogList(map);
        return setterPagedGrid(list, page);
    }

    /**
     * 判断是否已经点赞了这个视频
     */
    private boolean isLikedVlog(String userId, String vlogId) {
        String isLiked = redis.get(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);
        if ("1".equals(isLiked)) {
            return true;
        }
        return false;
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
        Page<Vlog> page_ = new Page<>();
        Page<Vlog> vlogPage = lambdaQuery()
                .eq(Vlog::getVlogerId, userId)
                .eq(Vlog::getIsPrivate, type)
                .page(page_);
        return setterPagedGrid(vlogPage.getRecords(), page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void like(String userId, String vlogerId, String vlogId) {
        MyLikedVlog myLikedVlog = new MyLikedVlog();
        myLikedVlog.setId(sid.nextShort());
        myLikedVlog.setUserId(userId);
        myLikedVlog.setVlogId(vlogId);
        myLikedVlogService.save(myLikedVlog);

        // 增加视频总赞数和博主总赞数
        redis.increment(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        redis.increment(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        // 保存用户和视频的点赞关系
        redis.set(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId, "1");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlike(String userId, String vlogerId, String vlogId) {
        myLikedVlogService.lambdaUpdate()
                .eq(MyLikedVlog::getUserId, userId)
                .eq(MyLikedVlog::getVlogId, vlogId)
                .remove();

        // 增加视频总赞数和博主总赞数
        redis.decrement(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + vlogerId, 1);
        redis.decrement(REDIS_VLOG_BE_LIKED_COUNTS + ":" + vlogId, 1);
        // 保存用户和视频的点赞关系
        redis.del(REDIS_USER_LIKE_VLOG + ":" + userId + ":" + vlogId);
    }
}

