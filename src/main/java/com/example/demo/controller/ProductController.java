package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author HuaZhongmin
 * @Date 2020/12/4
 * @Time 17:32
 * @Week 周五
 **/
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getAllProduct" , method = RequestMethod.GET)
    public List<Product> getAllProduct(HttpServletRequest request, HttpServletResponse response){
        Product product = new Product();
        return productService.getAllProduct(product);
    }



}
