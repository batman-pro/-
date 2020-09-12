package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setGroupItem(Map<String, Integer> map);

    Page<CheckGroup> findpage(String queryString);

    CheckGroup findbyid(Integer id);

    List<Integer> fbgroupiditem(Integer id);

    void editgroupone(CheckGroup checkGroup);

    void deleteGI(Integer id);

    void deleteG(Integer id);

    List<CheckGroup> findAll();

}
