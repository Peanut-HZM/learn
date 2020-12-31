package com.example.demo.elasticsearch;

import lombok.Data;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/18
 * @Time 17:48
 * @Week 周五
 **/
@Data
public class ElasticSearchDocument<T> {

    private String id;

    private T data;


    public ElasticSearchDocument(String id, T data) {
        this.id = id;
        this.data = data;
    }

    public ElasticSearchDocument() {

    }
}
