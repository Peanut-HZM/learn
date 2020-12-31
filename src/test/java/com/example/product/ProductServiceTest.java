package com.example.product;

import com.example.SpringBootApplicationTest;
import com.example.demo.elasticsearch.ElasticSearchClient;
import com.example.demo.elasticsearch.ElasticSearchDocument;
import com.example.demo.model.OperateLog;
import com.example.demo.service.OperateLogService;
import com.example.demo.service.ProductService;
import com.example.demo.threadPool.ThreadPool;
import com.example.demo.utils.GenerateIdUtils;
import com.example.demo.model.Product;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/4
 * @Time 17:54
 * @Week 周五
 **/

public class ProductServiceTest extends SpringBootApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceTest.class);

    volatile AtomicInteger pageNum = new AtomicInteger(1);

    volatile AtomicInteger productPageNum = new AtomicInteger(800);

    volatile PageInfo<Product> productPageInfo;

    volatile PageInfo<OperateLog> operateLogPageInfo;

    static Integer TOTAL_AMOUNT = 10000000;

    static Integer AVG_AMOUNT = 10000000;

    @Autowired
    ProductService productService;

    @Autowired
    OperateLogService operateLogService;

    @Autowired
    ElasticSearchClient elasticSearchClient;

    @Test
    public void getProducts() {
        Product product = new Product();
        List<Product> productList = productService.getAllProduct(product);
        System.out.println(productList);
    }

    @Test
    public void testSaveProduct() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TOTAL_AMOUNT; i++) {
            Product product = new Product();
            product.setId(GenerateIdUtils.getUUID());
            product.setName("test" + productService.getProductAmount());
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            product.setPrice(GenerateIdUtils.getNumberic());
            product.setType(GenerateIdUtils.getType());
            productService.saveProduct(product);
            product = null;
        }
        System.out.println("批量插入数据" + TOTAL_AMOUNT + "耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
    }

    @Test
    public void testThread() {
        long startTime = System.currentTimeMillis();
        long startTime1 = System.currentTimeMillis();
        Executor threadPool = ThreadPool.getThreadPool();

        //每次
        try {
            CountDownLatch countDownLatch = new CountDownLatch(AVG_AMOUNT);
            for (int i1 = 0; i1 < AVG_AMOUNT; i1++) {
                //每次单独创建新线程，浪费资源
//                createSingleThread();
                //自定义线程池的方式
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveOperate();
                        countDownLatch.countDown();
                    }
                });
            }
            //等待线程池中任务执行完成
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("批量插入数据" + AVG_AMOUNT + "耗时：" + (System.currentTimeMillis() - startTime1) + "毫秒");
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
    }

    private void createSingleThread() throws Exception {
        Runnable runnable = this::saveOperate;
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }

    private void saveOperate() {
        long startTime2 = System.currentTimeMillis();
        Product product = new Product();
        product.setId(GenerateIdUtils.getUUID());
//        int size = productService.getAllProduct(product).size();
        long size = productService.getProductAmount();
        product.setName("test" + size);
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        product.setPrice(GenerateIdUtils.getNumberic());
        product.setType(GenerateIdUtils.getType());
        productService.saveProduct(product);
        log.info("当前线程id:{}，线程名称：{}，操作一条数据耗时：{}毫秒", Thread.currentThread().getId(), Thread.currentThread().getName(), (System.currentTimeMillis() - startTime2));
//        System.out.println("当前线程：" + Thread.currentThread().getName() + "操作一条数据耗时：" + (System.currentTimeMillis() - startTime2));
    }

    @Test
    public void refreshElasticsearch() {
        String productIndex = "product";
        String operateLogIndex = "operate_log";
        try {
            boolean productExist = elasticSearchClient.isExistIndex("product");
            if (!productExist) {
                createESIndex(productIndex, Product.class);
            }
            boolean operateLogExist = elasticSearchClient.isExistIndex("operate_log");
            if (!operateLogExist) {
                createESIndex(operateLogIndex, OperateLog.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createESIndex(String esIndex, Class<?> indexClass) throws Exception {
        String indexType = "type";
        //保存索引的属性，索引的所有字段及其属性设置
        HashMap<String, Map<String, Object>> hashMap = new HashMap<>();
        Field[] declaredFields = indexClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //保存索引字段的属性设置，字段类型等
            HashMap<String, Object> map = new HashMap<>();
            String filedType = field.getGenericType().toString();
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID")) {
                continue;
            }
            //设置字段的属性
            if (filedType.contains("Date")) {
                map.put(indexType, "date");
            } else if (filedType.contains("BigDecimal") ||
                    filedType.contains("double") ||
                    filedType.contains("Double")) {
                map.put(indexType, "double");
            } else if (filedType.contains("Integer") || filedType.contains("int")) {
                map.put(indexType, "text");
            } else if (filedType.contains("Long") || filedType.contains("long")) {
                map.put(indexType, "text");
            } else if (filedType.contains("Byte") || filedType.contains("byte")) {
                map.put(indexType, "Array");
            } else if (filedType.contains("Array") || filedType.contains("List")) {
                map.put(indexType, "Array");
            } else if (filedType.contains("String")) {
                map.put(indexType, "text");
            } else if (filedType.contains("Object")) {
                map.put(indexType, "Object");
            } else if (filedType.contains("boolean")) {
                map.put(indexType, "boolean");
            }
            //设置索引的属性
            hashMap.put(fieldName, map);
        }
        boolean flag = elasticSearchClient.createIndex(esIndex, hashMap);
        if (flag) {
            log.info("索引{}创建成功，字段映射：{}", esIndex, hashMap);
        }
    }

    @Test
    public void pushProductDataToES(){
        int pageSize = 1000;
//        Executor threadPool = ThreadPool.getThreadPool();
//        CountDownLatch countDownLatch = new CountDownLatch(6000);
        while (true) {
//            threadPool.execute(() -> {
                PageHelper.startPage(productPageNum.get(), pageSize);
                Page<Product> allProduct = productService.getAllProduct(new Product());
                productPageInfo = new PageInfo<>(allProduct);
                List<Product> productList = productPageInfo.getList();
                List<ElasticSearchDocument<?>> documentArrayList = new ArrayList<>();
                for (Product product : productList) {
                    ElasticSearchDocument<Object> document = new ElasticSearchDocument<>(product.getId(),product);
                    documentArrayList.add(document);
                }
                try {
                    elasticSearchClient.saveAll("product", documentArrayList);
                    log.info("当前执行的线程：{}，操作的页码：{}" , Thread.currentThread().getName() , productPageNum.get());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("批量保存数据失败！！！");
                }
//            });
            if (!ObjectUtils.isEmpty(productPageInfo) && productPageInfo.isIsLastPage()){
                break;
            }
            productPageNum.getAndAdd(1);
        }
//        try {
//            countDownLatch.await();
//        }catch (Exception e){
//            e.printStackTrace();
//            log.info("多线程等待异常！！！！");
//        }
    }

    @Test
    public void pushOperateLogDataToES(){
        int pageSize = 1000;
//        Executor threadPool = ThreadPool.getThreadPool();
//        CountDownLatch countDownLatch = new CountDownLatch(7000);
        while (true) {
//            threadPool.execute(() -> {
                PageHelper.startPage(pageNum.get(), pageSize);
                Page<OperateLog> allProduct = operateLogService.getAllOperateLog(new OperateLog());
                operateLogPageInfo = new PageInfo<>(allProduct);
                List<OperateLog> operateLogList = operateLogPageInfo.getList();
                List<ElasticSearchDocument<?>> documentArrayList = new ArrayList<>();
                for (OperateLog operateLog : operateLogList) {
                    ElasticSearchDocument<Object> document = new ElasticSearchDocument<>(operateLog.getId(),operateLog);
                    documentArrayList.add(document);
                }
                try {
                    elasticSearchClient.saveAll("operate_log", documentArrayList);
                    log.info("当前执行的线程：{}，操作的页码：{}" , Thread.currentThread().getName() , pageNum.get());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("批量保存数据失败！！！");
                }
//            });
            if (!ObjectUtils.isEmpty(operateLogPageInfo) && operateLogPageInfo.isIsLastPage()){
                break;
            }
            pageNum.getAndAdd(1);
        }
//        try {
//            countDownLatch.await();
//        }catch (Exception e){
//            e.printStackTrace();
//            log.info("多线程等待异常！！！！");
//        }
    }

    @Test
    public void getProductFromESById(){
        String index = "operate_log";
        String documentId = "149ABE149C584E9583A33AD93568442F";
        try {
            OperateLog operateLog = elasticSearchClient.get(index, documentId, OperateLog.class);
            log.info("从Elasticsearch中获取数据：{}",operateLog);
        }catch (Exception e){
            e.printStackTrace();
            log.info("从Elasticsearch中获取数据：{}失败",documentId);
        }
    }

    @Test
    public void getProductListFromESByQuery(){
        String productIndex = "product";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.prefixQuery("name","test21"));
        searchSourceBuilder.size(10000);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        try {
            List<Product> products = elasticSearchClient.searchByQuery(productIndex, searchSourceBuilder, Product.class);
            log.info("ES中通过名称匹配查询到的数据：{}" , products);
        }catch (Exception e){
            e.printStackTrace();
            log.info("ES查询数据失败！！！！");
        }
    }


}
