package com.lee.springbootmall.dao.impl;

import com.lee.springbootmall.constant.ProductCategory;
import com.lee.springbootmall.dao.ProductDao;
import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    // Spring 自動注入 NamedParameterJdbcTemplate 實例
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //=====查詢商品列表加上商品分類篩選和使用者輸入篩選=====
    @Override
    public List<Product> getProducts(ProductCategory category, String search) {
        // 準備 SQL 查詢語句，目的是從 `product` 表中選取產品的所有資訊
        // sql後方增加WHERE 1=1不影響原本的查詢結果,當有送來category時可以拼接在後方增加篩選條件
        String sql = "SELECT product_id, product_name, category, image_url, price, " +
                "stock, description, created_date, last_modified_date FROM product WHERE 1=1";

        // 創建一個空的 HashMap，用於 namedParameterJdbcTemplate 的查詢參數。
        // 在這個查詢中實際上並沒有使用到任何參數，所以傳入一個空的 map。
        Map<String, Object> map = new HashMap<>();
        // 如果提供了產品類別的參數，則在 SQL 語句中添加條件來篩選特定類別的產品。
        if (category != null) {
            // 添加篩選條件(AND前記得要加上空白鍵)
            sql = sql + " AND category = :category";
            // 將篩選條件加入到參數 map 中, .name是將Enum類型轉為String類型
            map.put("category", category.name());
        }
        //實作使用者自行輸入查詢條件
        if (search != null) {
            // 添加篩選條件(AND前記得要加上空白鍵),不可把 % 直接加在 LIKE 後 %:search%
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + search + "%");
        }

        // 使用 namedParameterJdbcTemplate 執行查詢，並提供 SQL 語句、參數的 map
        // 以及一個 RowMapper 實例，這裡使用的是 ProductRowMapper，它會將 SQL 查詢的結果集映射到 Product 對象的列表中。
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        // 返回查詢到的產品列表。
        return productList;
    }

    // 實現 ProductDao 介面中的方法，根據商品ID查詢商品
    @Override
    public Product getProductById(Integer productId) {

        // SQL 查詢語句，使用冒號命名參數 ":productId" 來代表傳入的商品ID
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        // 設定 SQL 查詢中的參數，這裡是將 ":productId" 設為實際的商品ID值
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // 使用 NamedParameterJdbcTemplate 來執行 SQL 查詢，並指定 ProductRowMapper 來映射查詢結果
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        // 如果查詢結果非空，則返回第一個商品；否則返回空值
        if (!productList.isEmpty()) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    // 定義一個方法，用於新增產品至資料庫，傳入一個 ProductResquest 物件作為參數
    //這個方法主要是透過 JDBC 的 NamedParameterJdbcTemplate 來執行 SQL 語句，並使用命名參數的方式來傳遞參數值。
    //同時，使用 KeyHolder 來獲取自動生成的 productId，以便後續需要這個產品的唯一識別碼。
    @Override
    public Integer createProduct(ProductResquest productResquest) {
        // 準備 SQL 語句，使用命名參數 ":parameter" 的方式，避免 SQL 注入攻擊
        String sql = "INSERT INTO  product(PRODUCT_NAME, category, IMAGE_URL, PRICE, STOCK, DESCRIPTION, CREATED_DATE, LAST_MODIFIED_DATE) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        //創建一個map,將前端所傳來的參數一個一個放進map裡,用於存放 SQL 語句中的命名參數及其對應的值
        Map<String, Object> map = new HashMap<>();
        // 將 ProductResquest 物件的屬性值逐一放入 Map 中
        map.put("productName", productResquest.getProductName());
        map.put("category", productResquest.getCategory().toString());//記得要加上toString
        map.put("imageUrl", productResquest.getImageUrl());
        map.put("price", productResquest.getPrice());
        map.put("stock", productResquest.getStock());
        map.put("description", productResquest.getDescription());

        // 創建一個當前時間的 Date 物件，並將其放入 Map 中作為商品的創建時間和最後修改時間
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 使用 KeyHolder 來取得資料庫自動生成的 productId，這是為了在新增後能夠取得新建立的產品的唯一識別碼
        KeyHolder keyHolder = new GeneratedKeyHolder();
        // 使用 namedParameterJdbcTemplate 來執行 SQL 語句，並傳入命名參數和 KeyHolder
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        // 從 KeyHolder 中取得自動生成的 productId，轉換成 int 類型後返回
        int productId = keyHolder.getKey().intValue();
        // 返回新建立的產品的唯一識別碼
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductResquest productResquest) {
        // 準備 SQL 語句，使用命名參數 ":parameter" 的方式，避免 SQL 注入攻擊
        String sql = "UPDATE product SET " +
                "PRODUCT_NAME = :productName, category = :category, IMAGE_URL = :imageUrl, PRICE = :price," +
                "STOCK = :stock, DESCRIPTION = :description, LAST_MODIFIED_DATE = :lastModifiedDate " +
                "WHERE product_id = :productId";

        //創建一個map,將前端所傳來的參數一個一個放進map裡,用於存放 SQL 語句中的命名參數及其對應的值
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // 將 ProductResquest 物件的屬性值逐一放入 Map 中
        map.put("productName", productResquest.getProductName());
        map.put("category", productResquest.getCategory().toString());//記得要加上toString
        map.put("imageUrl", productResquest.getImageUrl());
        map.put("price", productResquest.getPrice());
        map.put("stock", productResquest.getStock());
        map.put("description", productResquest.getDescription());
        //記錄當下時間當成最後修改時間
        map.put("lastModifiedDate", new Date());
        // 使用 namedParameterJdbcTemplate 來執行 SQL 語句，並傳入命名參數和 KeyHolder
        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void deleteProductById(Integer productId) {

        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
