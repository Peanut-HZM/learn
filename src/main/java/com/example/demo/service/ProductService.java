package com.example.demo.service;

import com.example.demo.model.Product;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface ProductService {
    Integer saveProduct(Product product);

    Page<Product> getAllProduct(Product product);

    long getProductAmount();
}
