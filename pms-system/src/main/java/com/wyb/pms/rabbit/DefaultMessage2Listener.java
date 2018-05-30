package com.wyb.pms.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

public class DefaultMessage2Listener implements ChannelAwareMessageListener {


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        // logger.info("消费端接收到消息 : " + new String(body));
        System.out.println("消费端接收到消息2 : " + new String(body));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
