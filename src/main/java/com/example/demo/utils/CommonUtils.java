package com.example.demo.utils;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/7
 * @Time 13:38
 * @Week 周一
 **/
public class CommonUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    public static BigDecimal getNumberic(){
        double random = Math.random();
        double numberic = random * 10000;
        return BigDecimal.valueOf(numberic).setScale(2, RoundingMode.UP);
    }

    public static String getType(){
        double random = Math.random();
        int type = (int) (random * 10);
        return String.valueOf(type);
    }

    /***
     * 生成固定长度随机中文，kuojung
     * @param length 中文个数
     * @return 中文串
     * */
    public static String getStringZH(int length){
            StringBuilder result = new StringBuilder();
            String str = "";
            // Unicode中汉字所占区域\u4e00-\u9fa5,将4e00和9fa5转为10进制
            int start = Integer.parseInt("4e00", 16);
            int end = Integer.parseInt("9fa5", 16);

            for(int ic = 0; ic < length; ic ++){
                // 随机值
                int code = (new Random()).nextInt(end - start + 1) + start;
                // 转字符
                str = new String(new char[] { (char) code });
                result.append(str);
            }
            return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getStringZH(200));
    }

    @Test
    public void test(){
        System.out.println(getType());
    }
}
