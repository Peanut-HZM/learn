package com.example.demo.service.impl;

import com.example.demo.dao.OperateLogDao;
import com.example.demo.dao.ProductDao;
import com.example.demo.elasticsearch.ElasticSearchClient;
import com.example.demo.elasticsearch.ElasticSearchDocument;
import com.example.demo.model.OperateLog;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.ProductService;
import com.example.demo.utils.GenerateIdUtils;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/4
 * @Time 17:31
 * @Week 周五
 **/
@Component
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ElasticSearchClient elasticSearchClient;

    @Autowired
    ProductDao productDao;

    @Autowired
    OperateLogDao operateLogDao;

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Transactional//(rollbackFor = Exception.class)
    @Override
    public Integer saveProduct(Product product) {
        OperateLog operateLog = new OperateLog();
        operateLog.setId(GenerateIdUtils.getUUID());
        operateLog.setCreateTime(new Date());
        operateLog.setOperateCustomer("peanut");
        operateLog.setOperateStatus(String.valueOf(1));
        operateLog.setUpdateTime(new Date());
        operateLog.setOperateTable("T_PRODUCT");
        operateLog.setOperateType("INSERT");
        operateLog.setDataId(product.getId());
        operateLogDao.insert(operateLog);
        try {
            elasticSearchClient.save("product",new ElasticSearchDocument<>(product.getId(),product));
        }catch (Exception e){
            e.printStackTrace();
            log.info("数据：{} ，推送到ES失败" , product.getId());
        }
        try {
            elasticSearchClient.save("operate_log",new ElasticSearchDocument<>(operateLog.getId(),operateLog));
        }catch (Exception e){
            e.printStackTrace();
            log.info("数据：{} ，推送到ES失败" , operateLog.getId());
        }
        return productDao.insert(product);

//        return this.operateProduct(product);
    }

    @Override
    public Page<Product> getAllProduct(Product product) {
        ProductExample productExample = new ProductExample();
        Page<Product> products = productDao.selectByExample(productExample);
        return products;
    }

    public int operateProduct(Product product){
        int result = 0;
        OperateLog operateLog = new OperateLog();
        operateLog.setId(GenerateIdUtils.getUUID());
        operateLog.setCreateTime(new Date());
        operateLog.setOperateCustomer("peanut");
        operateLog.setOperateStatus(String.valueOf(1));
        operateLog.setUpdateTime(new Date());
        operateLog.setOperateTable("T_PRODUCT");
        operateLog.setOperateType("INSERT");
        operateLogDao.insert(operateLog);
        try {
            result = productDao.insert(product);
            int i = 1/0;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("======程序异常======");
        }
        return result;
    }

    @Override
    public long getProductAmount() {
        return productDao.countByExample(new ProductExample());
    }

}
