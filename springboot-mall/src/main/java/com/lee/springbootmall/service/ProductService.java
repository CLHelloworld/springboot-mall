package com.lee.springbootmall.service;

import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);
    //定義一個方法只接受前端送過來並且經過驗證後參數,會回傳一個productId,再去到impl實作
    Integer createProduct(ProductResquest productResquest);
}
