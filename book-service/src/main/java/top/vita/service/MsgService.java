package top.vita.service;

import top.vita.mo.MessageContent;
import top.vita.mo.MessageMO;

import java.util.List;
import java.util.Map;

/**
 * mongoDB消息服务
 *
 * @Author vita
 * @Date 2023/5/26 22:07
 */
public interface MsgService {

    void createMsg(MessageMO messageMO);

    List<MessageMO> queryList(String userId,
                              Integer page,
                              Integer pageSize);

    void deleteMsg(String fromUserId,
                   String toUserId,
                   Integer type,
                   String id);
}
