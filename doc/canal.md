# canal

## 1. 简介

canal 是Otter中间件的一部分，最初是解决异地机房数据同步的问题

Canal是阿里巴巴开源的一款基于数据库增量日志解析和订阅的系统，可以用于数据库的数据变更事件的捕获和处理。Canal可以将数据库的增量日志解析成数据变更事件，然后将这些事件发送给订阅者进行处理，从而实现实时同步数据库变更到其他系统

Canal的主要作用是用于解决数据库异构系统间的数据同步问题，例如将MySQL的数据同步到Elasticsearch、Redis、Hadoop等系统。同时，Canal还可以用于数据的实时监控、数据分析等场景。通过Canal，我们可以非常方便地将数据库的变化同步到其他系统，实现不同系统之间的数据交互和数据共享，大大提高了系统的数据一致性和效率

## 2. canal 的原理

canal是通过订阅mysql的binlog日志来进行数据的增量同步的。

首先我们了解一下mysql主从同步

![图片](https://mmbiz.qpic.cn/mmbiz_png/ia0pJaQvibib5TYO610g6jIgFh8G6vvGSvNKWVibicibCIGibeJvJAIarSVWBVcz7e2zmTtZMjFJT7LIOr1hliaibvnicdhA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

   master主库将改变记录，发送到二进制文件（binary log）中

  slave从库向mysql Master发送dump协议，将master主库的binary log events拷贝到它的中继日志（relay log）

  slave从库读取并重做中继日志中的事件，将改变的数据同步到自己的数据库，然后再保存自己的binlog

canal的工作原理：把自己伪装成slave，从master复制数据。读取binlog是需要master授权的，因为binlog是加密的，授权分用户名密码才能读。master授权后不知道读他的binlog的是从机还是canal，他的所有传输协议都符合从机的标准，所以master一直以为是从机读的。

## 3. 配置

MySQL binlog 配置

statement mixed row





canal rabbitmq

canal.properties

​	canal.serverMode = rabbitMQ

​	*rabbitmq.host = x.youlai.tech*

1. *rabbitmq.virtual.host =/*
2. *rabbitmq.exchange =canal.exchange*
3. *rabbitmq.username =guest*
4. *rabbitmq.password =guest*
5. *rabbitmq.deliveryMode =*

​	

