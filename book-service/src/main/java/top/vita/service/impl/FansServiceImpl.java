package top.vita.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.vita.enums.YesOrNo;
import top.vita.exceptions.GraceException;
import top.vita.grace.result.ResponseStatusEnum;
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

    @Autowired
    private Sid sid;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doFollow(String myId, String toId) {
        // 是否已经关注过
        Integer count = lambdaQuery()
                .eq(Fans::getFanId, myId)
                .eq(Fans::getVlogerId, toId)
                .count();
        if (count == 1){
            GraceException.display(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        Fans fans = new Fans();
        fans.setId(sid.nextShort());
        fans.setFanId(myId);
        fans.setVlogerId(toId);
        // 判断对方是否也是我的粉丝
        count = lambdaQuery()
                .eq(Fans::getFanId, toId)
                .eq(Fans::getVlogerId, myId)
                .count();
        if (count == 1) {
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
    }
}

