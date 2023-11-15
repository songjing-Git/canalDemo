package com.example.canal.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Service
public class RabbitMQReceiver {

    private final Logger logger = LoggerFactory.getLogger(RabbitMQReceiver.class);


    @RabbitListener(queues = "canal-queue")
    public void receiver(Message message) throws UnsupportedEncodingException {
        String s = new String(message.getBody(), "UTF-8");
        logger.info(" message is \n{}", s);

    }
}
