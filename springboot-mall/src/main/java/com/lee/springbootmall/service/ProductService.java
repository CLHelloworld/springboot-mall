package com.lee.springbootmall.service;

import com.lee.springbootmall.dto.ProductQueryParams;
import com.lee.springbootmall.dto.ProductRequest;
import com.lee.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    //分頁第二類
    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);

    //定義一個方法只接受前端送過來並且經過驗證後參數,會回傳一個productId,再去到impl實作
    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

}
