package com.wyb.pms.redis;

import com.wyb.pms.config.redis.RedisConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

public class RedisPublishClient {

    /*public static void main(String[] args){
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行: "+jedis.ping());

        jedis.publish("pubsub:queue", "helloworld");
    }*/

    RedisTemplate<String,Object> redisTemplate;

    public  void execute() {
        String channel = "pubsub:queue";
        redisTemplate.convertAndSend(channel, "from testData");
    }
    public static void main(String[] args) {
        ApplicationContext applicationContext   = new AnnotationConfigApplicationContext(RedisConfig.class);
        RedisPublishClient pubSubMain = new RedisPublishClient();
        pubSubMain.redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("redisTemplate");
        pubSubMain.execute();
    }
}
