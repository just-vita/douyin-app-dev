package top.vita.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.vita.enums.MessageEnum;
import top.vita.mo.MessageContent;
import top.vita.mo.MessageMO;
import top.vita.pojo.Users;
import top.vita.repository.MessageRepository;
import top.vita.service.FansService;
import top.vita.service.MsgService;
import top.vita.service.UsersService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private FansService fansService;

    @Override
    public void createMsg(String fromUserId,
                          String toUserId,
                          Integer type,
                          MessageContent msgContent) {
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

    @Override
    public List<MessageMO> queryList(String userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        // 根据userId查询消息
        List<MessageMO> list = messageRepository.findAllByToUserIdOrderByCreateTimeDesc(userId, pageable);
        for (MessageMO messageMO : list) {
            // 从mongoDB中取出的数据的时间会早8小时，处理时间
            try {
                messageMO.setCreateTime(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(messageMO.getCreateTime().toLocaleString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (messageMO.getMsgType() != null && MessageEnum.FOLLOW_YOU.type.equals(messageMO.getMsgType())){
                // 设置是否互关
                MessageContent msgContent = messageMO.getMsgContent();
                boolean isFollowed = fansService.isFollowed(userId, messageMO.getFromUserId());
                if (isFollowed) {
                    msgContent.setFriend(true);
                } else {
                    msgContent.setFriend(false);
                }
                messageMO.setMsgContent(msgContent);
            }
        }
        return list;
    }

    @Override
    public void deleteMsg(String fromUserId, String toUserId, Integer type, MessageContent messageContent) {
        messageRepository.deleteAllByFromUserIdAndToUserIdAndMsgTypeAndMsgContent(fromUserId, toUserId, type, messageContent);
    }
}
