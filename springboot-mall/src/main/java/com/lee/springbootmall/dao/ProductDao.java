package com.lee.springbootmall.dao;

import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts();
    Product getProductById(Integer productid);

    Integer createProduct(ProductResquest productResquest);

    void updateProduct(Integer productId, ProductResquest productResquest);

    void deleteProductById(Integer productId);
}