package com.wyb.pms.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DefaultMessage2Client implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("rabbit2 callback: "+ b + "  correlationData: "+correlationData + "  string: " +s);
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("rabbit2 i: "+ i + "  message: "+message + "  string: " +s + s1 + s2);
    }

    public void send(String routingkey, String message){
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertSendAndReceive("topicExchange", routingkey, message, correlationId);
    }
}
