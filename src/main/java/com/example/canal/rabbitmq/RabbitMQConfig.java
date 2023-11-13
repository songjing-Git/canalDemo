package com.example.canal.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //定义队列名
    private static final String QUEUE = "canal-queue";

    private static final String EXCHANGE = "canal-exchange";

    private static final String ROUTING_KEY = "routingKey";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }

    //创建交换机
    @Bean
    public DirectExchange exchange(){
        return new DirectExchange(EXCHANGE);
    }

    //将队列和交换机进行绑定
    @Bean
    public Binding binding01(){
        //将队列queue1和交换机进行绑定
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
