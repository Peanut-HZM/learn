package com.example.demo.rocketMQ.consumer;

import com.example.demo.rocketMQ.listener.MessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author peanut
 * @date 2021/3/5
 * @time 10:48 上午
 * @week 星期五
 **/

public class PushConsumer extends DefaultMQPushConsumer {

    private static DefaultMQPushConsumer consumer;

    private static final String CONSUME_GROUP = "peanut_consume";

//    private static final String NAME_SERVER_ADDRESS = "172.32.0.2:9876;172.32.0.3:9876";
    private static final String NAME_SERVER_ADDRESS = "192.168.43.49:9876;192.168.43.49:9877";

    private static final String CREATE_ORDER_TOPIC = "CREATE_ORDER_TOPIC";

    private static final String CREATE_ORDER_TAG = "CREATE_ORDER_TAG";

    private static DefaultMQPushConsumer getConsumer() throws MQClientException{
        if (ObjectUtils.isEmpty(consumer)){
            System.out.println("====创建消费者=====");
            //创建消费者对象
            consumer = new DefaultMQPushConsumer(CONSUME_GROUP);
            //设置MQ注册中心
            consumer.setNamesrvAddr(NAME_SERVER_ADDRESS);
            //设置消费模式，启动时从队列最后的位置开始消费
            //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            //设置消费的消息的topic和tag
            consumer.subscribe(CREATE_ORDER_TOPIC , "*");
        }
        return consumer;
    }

    public PushConsumer() throws MQClientException {
        //注册消费的监听，在监听中消费消息，并返回消息消费的状态
        getConsumer().registerMessageListener((MessageListenerConcurrently) (msgs , content) -> {
            try {
                for(Message msg : msgs){
                    String topic = msg.getTopic();
                    System.out.println("消费的消息的topic" + topic);
                    String tags = msg.getTags();
                    System.out.println("消费的消息的tags" + tags);
                    byte[] body = msg.getBody();
                    String msgBody = new String(body, StandardCharsets.UTF_8);
                    System.out.println("消费的消息的msgBody" + msgBody);
                }
            }catch (Exception e){
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        System.out.println("消费者启动成功");
    }

    public static void main(String[] args) {
        //注册消费的监听，在监听中消费消息，并返回消息消费的状态
        try {
            //创建消费者对象
            consumer = new DefaultMQPushConsumer(CONSUME_GROUP);
            //设置MQ注册中心
            consumer.setNamesrvAddr(NAME_SERVER_ADDRESS);
            //设置消费模式，启动时从队列最后的位置开始消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            //设置消费的消息的topic和tag
            consumer.subscribe(CREATE_ORDER_TOPIC , "*");
            //消费者注册监听器
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs , context) -> {
                try {
                    System.out.println("消费者监听：" + context);
                    for (Message msg : msgs) {
                        String topic = msg.getTopic();
                        System.out.println("消费的消息的topic：" + topic);
                        String tags = msg.getTags();
                        System.out.println("消费的消息的tags：" + tags);
                        byte[] body = msg.getBody();
                        String msgBody = new String(body, StandardCharsets.UTF_8);
                        System.out.println("消费的消息的msgBody：" + msgBody);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            //启动消费者
            consumer.start();
            System.out.println("消费者启动成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
