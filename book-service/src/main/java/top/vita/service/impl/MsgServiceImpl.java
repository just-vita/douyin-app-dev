package top.vita.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vita.mo.MessageMO;
import top.vita.pojo.Users;
import top.vita.repository.MessageRepository;
import top.vita.service.MsgService;
import top.vita.service.UsersService;

import java.util.Date;
import java.util.Map;

/**
 * @Author vita
 * @Date 2023/5/26 22:09
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UsersService usersService;

    @Override
    public void createMsg(String fromUserId,
                          String toUserId,
                          Integer type,
                          Map msgContent) {
        Users fromUser = usersService.getById(fromUserId);

        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickname(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());

        messageMO.setToUserId(toUserId);

        messageMO.setMsgType(type);
        if (msgContent != null){
            messageMO.setMsgContent(msgContent);
        }
        messageMO.setCreateTime(new Date());

        messageRepository.save(messageMO);
    }
}
