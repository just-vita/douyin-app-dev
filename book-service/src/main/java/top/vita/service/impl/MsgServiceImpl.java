package top.vita.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public List<MessageMO> queryList(String userId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        // 根据userId查询消息
        List<MessageMO> list = messageRepository.findAllByToUserIdOrderByCreateTimeDesc(userId, pageable);
        for (MessageMO messageMO : list) {
            if (messageMO.getMsgType() != null && MessageEnum.FOLLOW_YOU.type.equals(messageMO.getMsgType())) {
                // 设置是否互关
                boolean isFollowed = fansService.isFollowed(userId, messageMO.getFromUserId());
                if (isFollowed) {
                    messageMO.setFriend(true);
                } else {
                    messageMO.setFriend(false);
                }
            }
        }
        return list;
    }

    @Override
    public void createMsg(MessageMO messageMO) {
        Users fromUser = usersService.getById(messageMO.getFromUserId());

        messageMO.setFromNickname(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());

        // 直接处理时间
        messageMO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        messageRepository.save(messageMO);
    }

    @Override
    public void deleteMsg(MessageMO messageMO) {
        String fromUserId = messageMO.getFromUserId();
        String toUserId = messageMO.getToUserId();
        Integer type = messageMO.getMsgType();
        if (MessageEnum.FOLLOW_YOU.type.equals(type)) {
            messageRepository.deleteAllByFromUserIdAndToUserIdAndMsgType(fromUserId, toUserId, type);
        } else if (MessageEnum.LIKE_VLOG.type.equals(type)) {
            messageRepository.deleteAllByFromUserIdAndToUserIdAndMsgTypeAndVlogId(fromUserId, toUserId, type, messageMO.getVlogId());
        } else if (MessageEnum.COMMENT_VLOG.type.equals(type) ||
                   MessageEnum.REPLY_YOU.type.equals(type) ||
                   MessageEnum.LIKE_COMMENT.type.equals(type)) {
            messageRepository.deleteAllByFromUserIdAndToUserIdAndMsgTypeAndCommentId(fromUserId, toUserId, type, messageMO.getCommentId());
        }
    }
}
