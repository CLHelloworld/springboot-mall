package com.lee.springbootmall.controller;

import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    // 自動連結 ProductService 服務
    @Autowired
    private ProductService productService;

    // 處理 GET 請求，獲取特定 productId 的商品
    @GetMapping("/products/{productId}")
    //當前端來請求這個API時會回傳Product這個類型的Json給前端
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
    //@RequestBody 接住前端傳來一整包的東西
    //@Valid 若使用經由驗證過例如NotNull之類註解要加上這個註解
    // ProductResquest 經由dto驗證後回來的值
    @PostMapping("/products")
    public ResponseEntity<Product> creatProduct(@RequestBody @Valid ProductResquest productResquest){
        //從Controller實作進去
        //先預設productService會有createProduct這個方法,會去資料庫中創建商品後返回productId回來
        Integer productId = productService.createProduct(productResquest);
        //從資料庫取得商品數據,確認的動作
        Product product = productService.getProductById(productId);
        // 回傳狀態碼 HttpStatus.CREATED + 上一行取得的 product 給前端
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }




}