package com.lee.springbootmall.dao;

import com.lee.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productid);
}