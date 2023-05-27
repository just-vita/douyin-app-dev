package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.enums.MessageEnum;
import top.vita.enums.YesOrNo;
import top.vita.mo.MessageContent;
import top.vita.pojo.Comment;
import top.vita.pojo.Fans;
import top.vita.mapper.FansMapper;
import top.vita.service.FansService;
import top.vita.service.MsgService;
import top.vita.utils.PagedGridResult;
import top.vita.utils.RedisOperator;
import top.vita.vo.FansVO;
import top.vita.vo.VlogerVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.vita.service.base.BaseInfoProperties.*;

/**
 * 粉丝表

(Fans)表服务实现类
 *
 * @author vita
 * @since 2023-05-24 00:57:35
 */
@Service("fansService")
public class FansServiceImpl extends ServiceImpl<FansMapper, Fans> implements FansService {

    @Autowired
    private Sid sid;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private FansMapper fansMapper;
    @Autowired
    private MsgService msgService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doFollow(String myId, String toId) {
        // 如果已经关注对方，则直接返回
        if (isFollowed(myId, toId)){
            return;
        }
        Fans fans = new Fans();
        fans.setId(sid.nextShort());
        fans.setFanId(myId);
        fans.setVlogerId(toId);
        // 判断对方是否是我的粉丝
        boolean flag = isFollowed(toId, myId);
        if (flag) {
            // 改为互粉状态
            fans.setIsFanFriendOfMine(YesOrNo.YES.type);
            // 将对方也改为互粉状态
            lambdaUpdate()
                    .eq(Fans::getFanId, toId)
                    .eq(Fans::getVlogerId, myId)
                    .set(Fans::getIsFanFriendOfMine, YesOrNo.YES.type)
                    .update();
        } else{
            fans.setIsFanFriendOfMine(YesOrNo.NO.type);
        }
        save(fans);
        // 将关注/粉丝量进行自增
        redis.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.increment(REDIS_MY_FANS_COUNTS+ ":" + toId, 1);
        // 存储互关状态
        redis.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + toId, "1");

        // 向被关注方发送消息
        msgService.createMsg(myId, toId, MessageEnum.FOLLOW_YOU.type, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCancel(String myId, String toId) {
        // 判断对方是否是我的粉丝
        boolean flag = isFollowed(toId, myId);
        if (flag){
            // 将对方的互关状态抹除
            lambdaUpdate()
                    .eq(Fans::getFanId, toId)
                    .eq(Fans::getVlogerId, myId)
                    .set(Fans::getIsFanFriendOfMine, YesOrNo.NO.type)
                    .update();
        }
        // 删除我的关注记录
        lambdaUpdate()
                .eq(Fans::getFanId, myId)
                .eq(Fans::getVlogerId, toId)
                .remove();
        // 将关注/粉丝量进行自减
        redis.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redis.decrement(REDIS_MY_FANS_COUNTS+ ":" + toId, 1);
        // 删除互关状态
        redis.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + toId);

        // 清除关注消息
        msgService.deleteMsg(myId, toId, MessageEnum.FOLLOW_YOU.type, null);
    }

    /**
     * 判断对方是否是我的粉丝
     */
    public boolean isFollowed(String myId, String toId) {
        String isFollow = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + toId);
        if ("1".equals(isFollow)){
            return true;
        }
        return false;
    }

    @Override
    public PagedGridResult queryMyFollows(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);
        PageHelper.startPage(page, pageSize);
        List<VlogerVO> list = fansMapper.queryMyFollows(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryMyFans(String myId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("myId", myId);
        PageHelper.startPage(page, pageSize);
        List<FansVO> list = fansMapper.queryMyFans(map);
        for (FansVO fansVO : list) {
            // 判断我有没有关注我的粉丝
            boolean followed = isFollowed(myId, fansVO.getFanId());
            if (followed){
                fansVO.setFriend(true);
            }
        }
        return setterPagedGrid(list, page);
    }

}

