package com.example.demo.threadPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/16
 * @Time 18:14
 * @Week 周三
 **/
public class ThreadPool {

    private final static Logger log = LoggerFactory.getLogger(ThreadPool.class);

    /**
     * 核心线程数
     */
    private final static int CORE_POOL_SIZE = 20;

    /**
     * 最大线程数
     */
    private final static int MAX_POOL_SIZE = 40;

    /**
     * 线程最大存活时间，单位：秒
     */
    private final static int KEEP_ALIVE_TIME = 600;

    /**
     * 等待队列的最大容量
     */
    private final static int MAX_QUEUE_CAPACITY = Integer.MAX_VALUE;

    /**
     * 线程名称前缀设置
     */
    private final static String NAME_PREFIX = "ProductSave-";

    public static Executor getThreadPool(){
        long startTime = System.currentTimeMillis();
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数设置
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        //最大线程数设置
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        //线程最大空闲存活时间设置
        threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        //线程池的等待队列最大容量设置
        threadPoolTaskExecutor.setQueueCapacity(MAX_QUEUE_CAPACITY);
        //线程名字前缀设置
        threadPoolTaskExecutor.setThreadNamePrefix(NAME_PREFIX);
        /**
         * 设置线程池的拒绝策略
         * AbortPolicy()：不作处理，抛出异常
         * CallerRunsPolicy：由调用者所在线程执行
         * DiscardOldestPolicy()：将等待队列中的任务移除，放入新的任务
         * DiscardPolicy()：自定义被拒绝的任务的执行策略，默认不做处理
         */
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        threadPoolTaskExecutor.initialize();
        log.info("线程池初始化耗时：{}" , (System.currentTimeMillis() - startTime));
//        System.out.println("线程池初始化耗时：" + (System.currentTimeMillis() - startTime));
        return threadPoolTaskExecutor;
    }

}
