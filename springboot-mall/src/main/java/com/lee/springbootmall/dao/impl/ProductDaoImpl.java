package com.lee.springbootmall.dao.impl;

import com.lee.springbootmall.dao.ProductDao;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    // Spring 自動注入 NamedParameterJdbcTemplate 實例
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 實現 ProductDao 介面中的方法，根據商品ID查詢商品
    @Override
    public Product getProductById(Integer productId) {

        // SQL 查詢語句，使用冒號命名參數 ":productId" 來代表傳入的商品ID
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        // 設定 SQL 查詢中的參數，這裡是將 ":productId" 設為實際的商品ID值
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);

        // 使用 NamedParameterJdbcTemplate 來執行 SQL 查詢，並指定 ProductRowMapper 來映射查詢結果
        List<Product> productList = namedParameterJdbcTemplate.query(sql, paramMap, new ProductRowMapper());

        // 如果查詢結果非空，則返回第一個商品；否則返回空值
        if (!productList.isEmpty()) {
            return productList.get(0);
        } else {
            return null;
        }
    }
}
