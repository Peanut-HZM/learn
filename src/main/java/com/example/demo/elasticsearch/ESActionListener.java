package com.example.demo.elasticsearch;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/24
 * @Time 15:11
 * @Week 周四
 **/
public class ESActionListener implements ActionListener<IndexResponse> {

    @Override
    public void onResponse(IndexResponse indexResponse) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
