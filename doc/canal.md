# canal

## 1. 简介

 **canal 是Otter中间件的一部分，最初是解决异地机房数据同步的问题**

![19151450_AYEV](C:\Users\Administrator\Desktop\doc\img\19151450_AYEV.webp)

是阿里巴巴开源的一款基于数据库增量日志解析和订阅的系统，可以用于数据库的数据变更事件的捕获和处理。Canal可以将数据库的增量日志解析成数据变更事件，然后将这些事件发送给订阅者进行处理，从而实现实时同步数据库变更到其他系统

Canal的主要作用是

- 数据库镜像
- 数据库实时备份
- 索引构建和实时维护(拆分异构索引、倒排索引等)
- 业务 cache 刷新
- 带业务逻辑的增量数据处理

![68747470733a2f2f696d672d626c6f672e6373646e696d672e636e2f32303139313130343130313733353934372e706e67](C:\Users\Administrator\Desktop\doc\img\68747470733a2f2f696d672d626c6f672e6373646e696d672e636e2f32303139313130343130313733353934372e706e67.png)

## 2. canal 的原理

canal是通过订阅mysql的binlog日志来进行数据的增量同步的。

首先我们了解一下mysql主从同步

![963440-20180226223835065-798381309](C:\Users\Administrator\Desktop\doc\img\963440-20180226223835065-798381309.png)

从上层来看，复制分成三步：

- master将改变记录到二进制日志(binary log)中（这些记录叫做二进制日志事件，binary log events，可以通过show binlog events进行查看）；
- slave将master的binary log events拷贝到它的中继日志(relay log)；
- slave重做中继日志中的事件，将改变反映它自己的数据。

## 3. 配置

 **MySQL binlog 配置**
```mysql
SHOW VARIABLES LIKE 'log_bin';

show variables like 'binlog_format';
-- STATEMENT、ROW、MIXED
```




**canal 配置**
```properties
# tcp, kafka, rocketMQ, rabbitMQ, pulsarMQ 
canal.serverMode = tcp 

# rabbitMQ
rabbitmq.host = 127.0.0.1 
rabbitmq.virtual.host =/ 
rabbitmq.exchange =canal-exchange 
rabbitmq.username =admin 
rabbitmq.password =admin 
rabbitmq.deliveryMode =direct 

rabbitmq.queue = canal-queue 
rabbitmq.routingKey =canal-routing-key 
```

## 4.demo

