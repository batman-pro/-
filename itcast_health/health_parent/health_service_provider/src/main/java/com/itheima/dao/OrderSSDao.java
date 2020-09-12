package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;

public interface OrderSSDao {

    Long findById(Date orderDate);

    void upload(OrderSetting orderSetting);

    void insert(OrderSetting orderSetting);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    OrderSetting findByDate(Date date);


}
