package com.lee.springbootmall.dao;

import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productid);

    Integer createProduct(ProductResquest productResquest);

    void updateProduct(Integer productId, ProductResquest productResquest);
}