package com.lee.springbootmall.util;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public class Page<T> {

    private Integer limit;
    private Integer offset;
    private Integer total;
    //存放查詢出來的商品數據
    private List<T> results;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> result) {
        this.results = result;
    }
}
