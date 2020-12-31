package com.example;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/4
 * @Time 18:30
 * @Week 周五
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootApplicationTest {

    @Before
    public void before() {
        System.out.println("=======启动单元测试========");
    }

    @After
    public void after(){
        System.out.println("======单元测试结束======");
    }
}
