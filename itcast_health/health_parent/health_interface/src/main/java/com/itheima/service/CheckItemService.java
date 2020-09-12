package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    public void add(CheckItem checkItem);

    public PageResult findpage(Integer currentPage, Integer pageSize, String queryString);

    public void delete(Integer id);

    CheckItem findbyid(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();

}
