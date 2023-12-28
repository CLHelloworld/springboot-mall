package com.lee.springbootmall.service.impl;

import com.lee.springbootmall.dao.ProductDao;
import com.lee.springbootmall.dto.ProductQueryParams;
import com.lee.springbootmall.dto.ProductResquest;
import com.lee.springbootmall.model.Product;
import com.lee.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductResquest productResquest) {
        //實做完後再去Dao層
        return productDao.createProduct(productResquest);
    }

    @Override
    public void updateProduct(Integer productId, ProductResquest productResquest) {
        //預期Dao內有updateProduct()方法
        productDao.updateProduct(productId,productResquest);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);
    }
}
