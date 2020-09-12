package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void addForm(Setmeal setmeal);

    void addSG(Map<String, Integer> checkgroupId);

    Page<Setmeal> findpage(String queryString);

    void deleteSG(Integer id);

    void delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map<String,Object>> findCount();

}
