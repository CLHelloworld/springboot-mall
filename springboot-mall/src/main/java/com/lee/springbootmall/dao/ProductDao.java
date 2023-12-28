package com.lee.springbootmall.dao;

import com.lee.springbootmall.dto.ProductQueryParams;
import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    //分頁第二類
    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productid);

    Integer createProduct(ProductResquest productResquest);

    void updateProduct(Integer productId, ProductResquest productResquest);

    void deleteProductById(Integer productId);
}