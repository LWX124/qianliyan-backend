//package com.jeesite.modules.job.service;
//
//import com.alibaba.fastjson.JSON;
//import com.jeesite.modules.util2.AppRocket;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class Producer {
//    private final static Logger logger = LoggerFactory.getLogger(Producer.class);
//
//
//    public static void productSend(String source,String type,String userId,String userBId) throws MQClientException, InterruptedException {
//        logger.info("执行消费者方法");
//        //需要一个producer group名字作为构造方法的参数，这里为producer1
//        DefaultMQProducer producer = new DefaultMQProducer("producer1");
//
//        //设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
//        //NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
//        producer.setNamesrvAddr("127.0.0.1:9876");
//        producer.setVipChannelEnabled(false);
//
//        //为避免程序启动的时候报错，添加此代码，可以让rocketMq自动创建topickey
//        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");//AUTO_CREATE_TOPIC_KEY
//        producer.start();
//
//        try {
//            AppRocket appRocket = new AppRocket(source,type,userId,userBId);
//            String string = JSON.toJSONString(appRocket);
//            Message message = new Message("TopicTest", "Tag1",
//                    (string).getBytes(RemotingHelper.DEFAULT_CHARSET));
//
//            SendResult sendResult = producer.send(message);
//
//            logger.error("### 发送的消息ID # sendResult.getMsgId()={}",sendResult.getMsgId());
//            logger.error("### 发送消息的状态: # sendResult.getSendStatus()={}",sendResult.getSendStatus());
//
//
//        } catch (Exception e) {
//            logger.error("Producer执行失败！# error:{}", e);
//            Thread.sleep(1000);
//        }
//
//        producer.shutdown();
//
//    }
//}
