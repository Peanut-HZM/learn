package com.example.demo;

import org.junit.Test;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/8
 * @Time 17:32
 * @Week 周二
 **/
public class normalTest {

    @Test
    public void testCollectionSort(){

        class KeyValue {
            private String keyStr;
            private Integer valueNum;
            private String valueStr;

            public String getKeyStr() {
                return keyStr;
            }

            public void setKeyStr(String keyStr) {
                this.keyStr = keyStr;
            }

            public Integer getValueNum() {
                return valueNum;
            }

            public void setValueNum(Integer valueNum) {
                this.valueNum = valueNum;
            }

            public String getValueStr() {
                return valueStr;
            }

            public void setValueStr(String valueStr) {
                this.valueStr = valueStr;
            }

            @Override
            public String toString() {
                return valueStr;
            }
        }

        ArrayList<KeyValue> list = new ArrayList<>();
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = 1; i > 0; i++) {
            double random = Math.random() * 100;
            hashSet.add((int) random);
            if (hashSet.size() > 19){
                break;
            }
        }
        int j = 0;
        for (Integer num : hashSet) {
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr("str" + j );
            keyValue.setValueNum(num);
            keyValue.setValueStr(String.valueOf(num));
            list.add(keyValue);
            j ++;
        }
        System.out.println("排序前：" + list);
        list.sort(Comparator.comparing(KeyValue::getValueNum));
        System.out.println("根据int属性字段升序排序后：" + list);
        list.sort(Comparator.comparing(KeyValue::getValueStr));
        System.out.println("根据String属性字段排序后：" + list);
        list.sort((o1, o2) -> o2.getValueNum().compareTo(o1.getValueNum()));
        System.out.println("根据int属性字段降序排序后：" + list);
//        list.sort((o1, o2) -> Integer.valueOf(o2.getValueStr()).compareTo(Integer.valueOf(o1.getValueStr())));
        Collections.sort(list, new Comparator<KeyValue>() {
            @Override
            public int compare(KeyValue o1, KeyValue o2) {
                if (!ObjectUtils.isEmpty(o1) && !ObjectUtils.isEmpty(o2)){
                    String o1ValueStr = o1.getValueStr();
                    String o2ValueStr = o2.getValueStr();
                    if (!StringUtils.isEmpty(o1ValueStr) && !StringUtils.isEmpty(o2ValueStr)){
                        return Integer.valueOf(o1ValueStr).compareTo(Integer.valueOf(o2ValueStr));
                    }
                }
                return 0;
            }
        });
        System.out.println("根据String属性字段排序后：" + list);
    }



}
