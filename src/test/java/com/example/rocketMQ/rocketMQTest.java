package com.example.rocketMQ;

import com.example.demo.rocketMQ.consumer.PushConsumer;
import com.example.demo.rocketMQ.producer.Producer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author peanut
 * @date 2021/3/5
 * @time 11:25 上午
 * @week 星期五
 **/

public class rocketMQTest {

    private static final String CREATE_ORDER_TOPIC = "CREATE_ORDER_TOPIC";

    private static final String CREATE_ORDER_TAG = "CREATE_ORDER_TAG";

    private Producer producer;

    private PushConsumer pushConsumer;

    public Producer getProducer(){
        if (ObjectUtils.isEmpty(producer)){
            producer = new Producer();
        }
        return producer;
    }

    public PushConsumer getConsumer() throws MQClientException {
        if (ObjectUtils.isEmpty(pushConsumer)){
            pushConsumer = new PushConsumer();
        }
        return pushConsumer;
    }

    @Test
    public void testProducer(){
        String[] placeArray = new String[]{"深圳", "北京", "上海", "广州", "杭州", "武汉", "南京", "成都", "长沙", "西安", "重庆", "厦门"};
        int i = 0;
        while (i < 10) {
            for (String place : placeArray) {
                Message message = new Message(CREATE_ORDER_TOPIC, CREATE_ORDER_TAG, UUID.randomUUID().toString(),  place.getBytes(StandardCharsets.UTF_8));
                try {
                    DefaultMQProducer mqProducer = getProducer().getProducer();
                    SendResult result = mqProducer.send(message);
                    System.out.println(result.toString());
                    System.out.println("消息发送成功！！！" + message.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("消息发送失败:" + message.toString());
                }
            }
            i++;
        }
    }

    @Test
    public void testConsumer(){
        while (true) {
            try {
                PushConsumer pushConsumer = getConsumer();
                pushConsumer.getPullThresholdForTopic();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
