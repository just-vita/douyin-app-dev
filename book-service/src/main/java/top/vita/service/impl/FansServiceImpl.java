package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.enums.YesOrNo;
import top.vita.pojo.Fans;
import top.vita.mapper.FansMapper;
import top.vita.service.FansService;
import top.vita.utils.RedisOperator;

import static top.vita.base.BaseInfoProperties.*;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doFollow(String myId, String toId) {
        Fans fans = new Fans();
        fans.setId(sid.nextShort());
        fans.setFanId(myId);
        fans.setVlogerId(toId);
        // 判断对方是否是我的粉丝
        boolean flag = isFollowingMe(myId, toId);
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
    }

    /**
     * 判断对方是否是我的粉丝
     */
    private boolean isFollowingMe(String myId, String toId) {
        return lambdaQuery()
                .eq(Fans::getFanId, toId)
                .eq(Fans::getVlogerId, myId)
                .count() == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCancel(String myId, String toId) {
        // 判断对方是否是我的粉丝
        boolean flag = isFollowingMe(myId, toId);
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
    }
}

