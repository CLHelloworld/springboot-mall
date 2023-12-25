package com.lee.springbootmall.rowmapper;

import com.lee.springbootmall.constant.ProductCatedory;
import com.lee.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        // product.setCategory(resultSet.getString("category"));
        // 跟 model 的 VO 有關 String和Enum的轉換
        // 創一個字串變數去接住resultSet出來的值,再將字串轉換成Enum類型
        String categoryStr = resultSet.getString("category");
        ProductCatedory catedory = ProductCatedory.valueOf(categoryStr);
        product.setCategory(catedory);
        //為上方的簡寫,將資料庫中取出來的字串轉換為ProductCatedory的Enum值,傳入到set方法做設定
        //product.setCategory(ProductCatedory.valueOf(resultSet.getString("category")));

        product.setImageUrl(resultSet.getString("image_url"));
        product.setPrice(resultSet.getInt("price"));
        product.setStock(resultSet.getInt("stock"));
        product.setDescription(resultSet.getString("description"));
        product.setCreatedDate(resultSet.getDate("created_date"));
        product.setLastModifiedDate(resultSet.getDate("last_modified_date"));

        return product;
    }
}
