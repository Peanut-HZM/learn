package com.example.demo.rocketMQ.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author peanut
 * @date 2021/3/8
 * @time 9:56 上午
 * @week 星期一
 **/

public class MessageListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            System.out.println("消费者监听：" + consumeConcurrentlyContext);
            for (Message msg : list) {
                String topic = msg.getTopic();
                System.out.println("消费的消息的topic" + topic);
                String tags = msg.getTags();
                System.out.println("消费的消息的tags" + tags);
                byte[] body = msg.getBody();
                String msgBody = new String(body, StandardCharsets.UTF_8);
                System.out.println("消费的消息的msgBody" + msgBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
