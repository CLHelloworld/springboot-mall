package com.lee.springbootmall.controller;

import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    // 自動連結 ProductService 服務
    @Autowired
    private ProductService productService;

    //=====查詢商品列表=====
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProduct(){
        List<Product>productList = productService.getProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    // 處理 GET 請求，獲取特定 productId 的商品
    @GetMapping("/products/{productId}")
    //當前端來請求這個API時會回傳Product這個類型的Json給前端
    // getProduct() 方法表示 productId 這個值是從URL路徑來的
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {

        // 透過 productService.getProductById 方法查詢資料庫中特定商品的數據
        Product product = productService.getProductById(productId);

        // 找到商品，則返回HTTP 狀態碼 OK (200) 和商品數據
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            // 如果找不到商品，返回 HTTP 狀態碼 NOT FOUND (404)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //@RequestBody 接住前端傳來一整包的東西
    //@Valid 若使用經由驗證過例如NotNull之類註解要加上這個註解
    // ProductResquest 經由dto驗證後回來的值
    @PostMapping("/products")
    public ResponseEntity<Product> creatProduct(@RequestBody @Valid ProductResquest productResquest) {
        //從Controller實作進去
        //先預設productService會有createProduct這個方法,會去資料庫中創建商品後返回productId回來
        Integer productId = productService.createProduct(productResquest);
        //從資料庫取得商品數據,確認的動作
        Product product = productService.getProductById(productId);
        // 回傳狀態碼 HttpStatus.CREATED + 上一行取得的 product 給前端
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @PutMapping("/products/{productId}")
    //接住從路徑來的productId值,@RequestBody接住前端來的參數
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductResquest productResquest){
        // 透過 productService 的 getProductById 方法，嘗試根據 productId 獲取產品資訊。
        Product product = productService.getProductById(productId);

        //=====判斷商品是否存在=====
        // 判斷獲取的產品資訊是否存在。
        // 若沒有加上這段判斷,若為null會回傳200給前端並不是404,加上閱讀性比較好
        if(product == null){
            // 如果 product 為 null，說明沒有找到對應的產品資訊，
            // 那麼就返回一個 HttpStatus.NOT_FOUND（404 狀態碼）的回應，表示資源未找到。
            //.build() 方法不帶任何正文內容，只返回一個包含狀態碼和標頭的 ResponseEntity 對象。
            // 這個方法通常用於創建沒有正文，只需要通過狀態碼來表達的 HTTP 響應，例如 404 Not Found 或 204 No Content。
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //=====修改數據=====
        //預期productService會有updateProduct(Id,修改過的值)方法
        productService.updateProduct(productId,productResquest);
        //productService.getProductById(productId) 從資料庫取得更新後的數據
        Product updateProduct = productService.getProductById(productId);
        //回傳狀態碼和數據傳給前端
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}