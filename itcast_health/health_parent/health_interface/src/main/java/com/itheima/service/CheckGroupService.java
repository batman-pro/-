package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findpage(QueryPageBean queryPageBean);

    CheckGroup findbyid(Integer id);

    List<Integer> fbgroupiditem(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
