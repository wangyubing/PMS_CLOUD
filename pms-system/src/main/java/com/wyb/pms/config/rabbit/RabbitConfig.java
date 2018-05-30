package com.wyb.pms.config.rabbit;

import com.wyb.pms.rabbit.DefaultMessage2Listener;
import com.wyb.pms.rabbit.DefaultMessageListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class RabbitConfig {


    @Bean
    @ConfigurationProperties(prefix = "mq.rabbit")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    //声明队列
    @Bean
    public Queue queue1() {
        return new Queue("hello.queue1", true); // true表示持久化该队列
    }

    @Bean
    public Queue queue2() {
        return new Queue("hello.queue2", true);
    }

    //声明交互器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    //绑定
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("key.1");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with("key.#");
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue1());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new DefaultMessageListener());
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue2());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new DefaultMessage2Listener());
        return container;
    }
}
