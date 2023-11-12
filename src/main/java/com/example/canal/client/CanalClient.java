package com.example.canal.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

@Component
public class CanalClient {

    private final Logger logger = LoggerFactory.getLogger(CanalClient.class);

    private final List<CanalEntry.EventType> list = Arrays.asList(CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE, CanalEntry.EventType.UPDATE);

    public void run() throws Exception {
        String ip = "110.40.128.13";
        String destination = "example";
        //创建连接对象
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(ip, 11111), destination, "", ""
        );
        canalConnector.connect();
        canalConnector.subscribe("canal.canal_test01");
        canalConnector.rollback();

        while (true) {
            com.alibaba.otter.canal.protocol.Message message = canalConnector.getWithoutAck(1);
            long id = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            //判空
            if (CollectionUtils.isEmpty(entries)) {
                logger.info(" entries is null ");
                Thread.sleep(1000);
                continue;
            }
            for (CanalEntry.Entry entry : entries) {
                String tableName = entry.getHeader().getTableName();

                CanalEntry.EntryType entryType = entry.getEntryType();

                ByteString storeValue = entry.getStoreValue();

                if (entryType == CanalEntry.EntryType.ROWDATA) {

                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                    //获取操作类型
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    if (list.contains(eventType)) {
                        //拿到行数据
                        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();

                        for (CanalEntry.RowData rowData : rowDatasList) {
                            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            logger.info("tableName is {} row data is {}", tableName, rowData);
                            for (CanalEntry.Column column : beforeColumnsList) {
                                logger.info(" before data is  name {} value is {}", column.getName(), column.getValue());
                            }

                            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                            for (CanalEntry.Column column : afterColumnsList) {
                                logger.info(" after data is  name {} value is {}", column.getName(), column.getValue());
                            }
                        }
                    }
                }
            }
            canalConnector.ack(id);
        }
    }
}
