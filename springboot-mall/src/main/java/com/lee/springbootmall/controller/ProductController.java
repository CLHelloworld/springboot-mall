package com.lee.springbootmall.controller;

import com.lee.springbootmall.constant.ProductCategory;
import com.lee.springbootmall.dto.ProductQueryParams;
import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.service.ProductService;
import com.lee.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    // 自動連結 ProductService 服務
    @Autowired
    private ProductService productService;

    //=====查詢商品列表加上商品分類篩選和使用者自行輸入篩選(加上可選參數)=====
//    //===== 分頁第一類 =====
//    @GetMapping("/products")
//    public ResponseEntity<List<Product>> getProduct(
//
//            //===== 查詢條件 Filtering =====
//
//            //加上category請求,前端可以透過傳進來的category值,指定分類的商品
//            //@RequestParam 從URL路徑中取參數
//            //@RequestParam(required = false)設定category為可選參數
//            @RequestParam(required = false) ProductCategory category,
//            //使用者輸入查詢的關鍵字
//            @RequestParam(required = false) String search,
//
//            //===== 排序 Sorting =====
//
//            //根據某個欄位排序(預設根據created_date欄位排序)
//            @RequestParam(defaultValue = "created_date") String orderBy,
//            //使用升序或降序(預設 desc 大到小)
//            @RequestParam(defaultValue = "desc") String sort,
//
//            //=====分頁 Pagination=====
//
//            //取得幾筆數據 預設5 上限1000 下限0,上下限的設定需要在class上加上@Validated註解才會生效
//            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
//            //跳過多少筆數據
//            @RequestParam(defaultValue = "0") @Min(0) Integer offset
//
//    ) {
//        //將前端傳來的值set到productQueryParams一路傳到Dao層
//        ProductQueryParams productQueryParams = new ProductQueryParams();
//        productQueryParams.setCategory(category);
//        productQueryParams.setSearch(search);
//        productQueryParams.setOrderBy(orderBy);
//        productQueryParams.setSort(sort);
//        productQueryParams.setLimit(limit);
//        productQueryParams.setOffset(offset);
//
//        //將本來的category和search參數改成productQueryParams變數
//        //這樣就不用一直修改每一層的參數
//        List<Product> productList = productService.getProducts(productQueryParams);
//        return ResponseEntity.status(HttpStatus.OK).body(productList);
//    }
    //===== 分頁第二類 =====
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProduct(

            //===== 查詢條件 Filtering =====

            //加上category請求,前端可以透過傳進來的category值,指定分類的商品
            //@RequestParam 從URL路徑中取參數
            //@RequestParam(required = false)設定category為可選參數
            @RequestParam(required = false) ProductCategory category,
            //使用者輸入查詢的關鍵字
            @RequestParam(required = false) String search,

            //===== 排序 Sorting =====

            //根據某個欄位排序(預設根據created_date欄位排序)
            @RequestParam(defaultValue = "created_date") String orderBy,
            //使用升序或降序(預設 desc 大到小)
            @RequestParam(defaultValue = "desc") String sort,

            //=====分頁 Pagination=====

            //取得幾筆數據 預設5 上限1000 下限0,上下限的設定需要在class上加上@Validated註解才會生效
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            //跳過多少筆數據
            @RequestParam(defaultValue = "0") @Min(0) Integer offset

    ) {
        //將前端傳來的值set到productQueryParams一路傳到Dao層
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);


        //取得 product List
        //流程說明 : 使用productService的getProducts方法根據查詢條件取得對應商品數據存放在productList內
        //再將商品列表存放到page的result變數,最後再將整個page傳給前端
        List<Product> productList = productService.getProducts(productQueryParams);
        //取得total的方法
        Integer total = productService.countProduct(productQueryParams);

        //分頁
        //new一個Page類型的Product
        Page<Product> page =new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        //代表當前查詢條件底下總共有多少數據
        page.setTotal(total);
        //將上方List查詢到商品數據的的值放到result變數內回傳前端
        page.setResults(productList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
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
                                                 @RequestBody @Valid ProductResquest productResquest) {
        // 透過 productService 的 getProductById 方法，嘗試根據 productId 獲取產品資訊。
        Product product = productService.getProductById(productId);

        //=====判斷商品是否存在=====
        // 判斷獲取的產品資訊是否存在。
        // 若沒有加上這段判斷,若為null會回傳200給前端並不是404,加上閱讀性比較好
        if (product == null) {
            // 如果 product 為 null，說明沒有找到對應的產品資訊，
            // 那麼就返回一個 HttpStatus.NOT_FOUND（404 狀態碼）的回應，表示資源未找到。
            //.build() 方法不帶任何正文內容，只返回一個包含狀態碼和標頭的 ResponseEntity 對象。
            // 這個方法通常用於創建沒有正文，只需要通過狀態碼來表達的 HTTP 響應，例如 404 Not Found 或 204 No Content。
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //=====修改數據=====
        //預期productService會有updateProduct(Id,修改過的值)方法
        productService.updateProduct(productId, productResquest);
        //productService.getProductById(productId) 從資料庫取得更新後的數據
        Product updateProduct = productService.getProductById(productId);
        //回傳狀態碼和數據傳給前端
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}