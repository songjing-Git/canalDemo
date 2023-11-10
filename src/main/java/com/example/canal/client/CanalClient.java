package com.example.canal.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class CanalClient {

    private final Logger logger = LoggerFactory.getLogger(CanalClient.class);

    public  void run() {
        String ip = "110.40.128.13";
        String destination = "example";
        //创建连接对象
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(ip, 11111), destination, "", ""
        );
        canalConnector.connect();
        canalConnector.subscribe(".*\\\\..*");
        canalConnector.rollback();
        int batchSize = 1;
        try {
            com.alibaba.otter.canal.protocol.Message message = canalConnector.getWithoutAck(1);
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0) {
                logger.debug("message : {}", message);
                Thread.sleep(1000);
            } else {
                logger.debug("message : {}", message);
            }
            canalConnector.ack(batchId);
            //当队列里面堆积的sql大于一定数值的时候就模拟执行
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
