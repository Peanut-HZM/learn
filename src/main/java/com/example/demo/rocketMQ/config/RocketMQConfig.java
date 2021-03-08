package com.example.demo.rocketMQ.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author peanut
 * @date 2021/3/5
 * @time 10:44 上午
 * @week 星期五
 **/

@Configuration
public class RocketMQConfig {

    @Value("${rocketMq.nameServer}")
    private String NAME_SERVER;



}
