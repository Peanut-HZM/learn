package com.example.demo.rocketMQ.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * @author peanut
 * @date 2021/3/5
 * @time 10:47 上午
 * @week 星期五
 **/
@Component
public class Producer {

    private static final String PRODUCER_GROUP = "peanut_group";

//    private static final String NAME_SERVER_ADDRESS = "172.32.0.2:9876;172.32.0.3:9876";
    private static final String NAME_SERVER_ADDRESS = "192.168.43.49:9876;192.168.43.49:9877";

    private DefaultMQProducer producer;

    public Producer(){

        System.out.println("创建生产者");
        //创建生产者
        producer = new DefaultMQProducer(PRODUCER_GROUP);

        //不开启VIP通道  此设置为true，端口号会减2
        producer.setVipChannelEnabled(false);

        //设置MQ的注册中心
        producer.setNamesrvAddr(NAME_SERVER_ADDRESS);

        //执行初始化方法
        start();
    }

    /**
     * 生产者使用前初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 对外提供获取生产者的接口
     * @return
     */
    public DefaultMQProducer getProducer(){
        return this.producer;
    }

    /**
     * 关闭生产者
     */
    public void shutDown(){
        this.producer.shutdown();
    }
}
