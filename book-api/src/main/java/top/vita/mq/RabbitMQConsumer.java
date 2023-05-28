package top.vita.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.vita.enums.MessageEnum;
import top.vita.exceptions.GraceException;
import top.vita.grace.result.ResponseStatusEnum;
import top.vita.mo.MessageMO;
import top.vita.service.MsgService;
import top.vita.service.base.RabbitMQConfig;
import top.vita.utils.JsonUtils;

import static top.vita.service.base.RabbitMQConfig.ADD_MSG_ROUTE;
import static top.vita.service.base.RabbitMQConfig.DEL_MSG_ROUTE;

/**
 * @Author vita
 * @Date 2023/5/28 16:20
 */
@Component
public class RabbitMQConsumer {

    @Autowired
    private MsgService msgService;

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYS_MSG})
    public void watchQueue(String payload, Message message){
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        MessageMO messageMO = JsonUtils.jsonToPojo(payload, MessageMO.class);
        if (messageMO == null){
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        if (routingKey.startsWith(ADD_MSG_ROUTE)) {
            msgService.createMsg(messageMO);
        } else if (routingKey.startsWith(DEL_MSG_ROUTE)) {
            msgService.deleteMsg(messageMO);
        } else {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
    }
}
