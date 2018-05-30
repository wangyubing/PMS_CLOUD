package com.wyb.pms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisSubcribeListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message);
    }
}
