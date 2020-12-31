package com.example.singleTest;

import com.example.demo.utils.GenerateIdUtils;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/14
 * @Time 16:57
 * @Week 周一
 **/
public class SingleTest {


    @Test
    public void testList(){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Integer.valueOf(GenerateIdUtils.getType()));
        }
        List<List<Integer>> partition = Lists.partition(list, 20);
        List<Integer> list1 = partition.get(0);
        ArrayList<Integer> list2 = new ArrayList<>(list1);
        if (list1 instanceof Serializable){
            System.out.println("list1序列化");
        }else {
            System.out.println("list1未序列化");
        }
        if (list2 instanceof Serializable){
            System.out.println("list2序列化");
        }else {
            System.out.println("list2未序列化");
        }
    }

}
