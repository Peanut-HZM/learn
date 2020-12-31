package com.example.demo.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/18
 * @Time 16:36
 * @Week 周五
 **/
@Data
public class ElasticEntity {

    private String id;

    private JSONObject data;
}
