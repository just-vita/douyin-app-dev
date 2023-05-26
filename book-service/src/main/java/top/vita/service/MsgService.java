package top.vita.service;

import top.vita.mo.MessageMO;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * mongoDB消息服务
 *
 * @Author vita
 * @Date 2023/5/26 22:07
 */
public interface MsgService {

    void createMsg(String fromUserId,
                   String toUserId,
                   Integer type,
                   Map msgContent);

    List<MessageMO> queryList(String userId,
                              Integer page,
                              Integer pageSize);

}
