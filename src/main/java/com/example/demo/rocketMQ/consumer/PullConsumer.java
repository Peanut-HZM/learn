package com.example.demo.rocketMQ.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author peanut
 * @date 2021/3/8
 * @time 4:33 下午
 * @week 星期一
 **/

public class PullConsumer {

    private static final Map<MessageQueue , Long> offsetTable = new HashMap<>();

    private static final String PULL_CONSUME_GROUP = "PEANUT_PULL_CONSUMER";

    private static final String CREATE_ORDER_TOPIC = "CREATE_ORDER_TOPIC";

    private static final String CREATE_ORDER_TAG = "CREATE_ORDER_TAG";

    private static final String NAME_SERVER_ADDRESS = "192.168.43.49:9876;192.168.43.49:9877";

    private static final String PULL_FOUND = "FOUND";

    public static Long  getMessageQueueOffset(MessageQueue messageQueue) {
        Long offset = offsetTable.get(messageQueue);
        return offset == null ? 0:offset;
    }

    public static void setOffsetTable(MessageQueue messageQueue , Long offset){
        offsetTable.put(messageQueue, offset);
    }

    public static void main(String[] args) {
        //创建Pull方式消费的消费者
        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer(PULL_CONSUME_GROUP);
        //设置注册中心
        pullConsumer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        try {
            //启动pullConsumer
            pullConsumer.start();
            //手动拉去消息进行消费
            while (true) {
                Set<MessageQueue> messageQueueSet = pullConsumer.fetchSubscribeMessageQueues(CREATE_ORDER_TOPIC);

                for (MessageQueue messageQueue : messageQueueSet) {
                    System.out.println("当前获取的队列：" + messageQueue.getQueueId());

                    PullResult pullResult = pullConsumer.pullBlockIfNotFound(messageQueue, null, getMessageQueueOffset(messageQueue), 32);

                    if (PULL_FOUND.equals(pullResult.getPullStatus().toString())) {
                        List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                        for (MessageExt messageExt : messageExtList) {
                            byte[] body = messageExt.getBody();
                            String topic = messageExt.getTopic();
                            System.out.println("消费的消息topic：" + topic);
                            String tags = messageExt.getTags();
                            System.out.println("消费的消息tags：" + tags);
                            String msgBody = new String(body, StandardCharsets.UTF_8);
                            System.out.println("消费的消息msgBody：" + msgBody);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
