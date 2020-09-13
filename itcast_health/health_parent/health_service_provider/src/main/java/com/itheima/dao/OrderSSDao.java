package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.OrderSettinglist;

import java.util.Date;

public interface OrderSSDao {

    Long findById(Date orderDate);

    void upload(OrderSetting orderSetting);

    void insert(OrderSetting orderSetting);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    OrderSetting findByDate(Date date);


    Page<OrderSettinglist> findpage(String queryString);


}
