package com.example.demo.rocketMQ.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @author peanut
 * @date 2021/3/5
 * @time 2:36 下午
 * @week 星期五
 **/

public class ProducerCallBack implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {
        System.out.println("消费者消费消息成功：" + sendResult.getMsgId());
    }

    @Override
    public void onException(Throwable throwable) {
        System.out.println("消费者消费消息失败：" + throwable.getMessage());
    }

}
