package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/18
 * @Time 16:50
 * @Week 周五
 **/
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.schema}")
    private String schema;

    private HttpHost getHttpHost(){
        return new HttpHost(host,port,schema);
    }

    @Bean
    public RestClientBuilder restClientBuilder(){
        return RestClient.builder(getHttpHost());
    }

    @Bean
    public RestClient restClient(){
        return restClientBuilder().build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder);
    }



}
