package com.lee.springbootmall.controller;

import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    // 自動連結 ProductService 服務
    @Autowired
    private ProductService productService;

    // 處理 GET 請求，獲取特定 productId 的商品
    @GetMapping("/products/{productId}")
    // getProduct() 方法表示 productId 這個值是從上方路徑來的
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){

        // 透過 productService 的 getProductById 方法查詢資料庫中特定商品的數據
        Product product = productService.getProductById(productId);

        // 如果找到商品，返回 HTTP 狀態碼 OK (200) 和商品數據
        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            // 如果找不到商品，返回 HTTP 狀態碼 NOT FOUND (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}