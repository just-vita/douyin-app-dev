package top.vita.service.base;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.vita.mo.MessageMO;
import top.vita.utils.JsonUtils;

import java.util.Objects;

/**
 * @Author vita
 * @Date 2023/5/28 16:13
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_MSG = "exchange_msg";

    public static final String QUEUE_SYS_MSG = "queue_sys_msg";

    public static final String ADD_MSG_ROUTE = "sys.msg.add";

    public static final String DEL_MSG_ROUTE = "sys.msg.del";

    @Bean(EXCHANGE_MSG)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_MSG)
                .durable(true)
                .build();
    }

    @Bean(QUEUE_SYS_MSG)
    public Queue queue(){
        return new Queue(QUEUE_SYS_MSG);
    }

    @Bean
    public Binding binding(@Qualifier(EXCHANGE_MSG) Exchange exchange,
                           @Qualifier(QUEUE_SYS_MSG) Queue queue){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("sys.msg.*")
                .noargs();
    }


}
