package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.AddPhone;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface OrderSettingService {

    void upload(List<OrderSetting> orderSettingList);

    PageResult findpage(QueryPageBean queryPageBean);

    List<Setmeal> findadd();


    void add(Integer[] setmealIds, AddPhone addPhone);

}
