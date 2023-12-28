package com.lee.springbootmall.dto;

import com.lee.springbootmall.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;

//dto放前端傳過來要驗證的變數  專門配合VO開一個class來根據前端會傳過來的參數做驗證
public class ProductResquest {

//    private Integer productId;//自增主鍵不是前端會傳過來的參數
    @NotNull
    private String productName;
    @NotNull
    private ProductCategory category;
    @NotNull
    private String imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;

    private String description;

//    private Date createdDate; //這兩種Data會由Spring boot程式設定
//    private Date lastModifiedDate;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
