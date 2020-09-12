package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSSDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSSDao orderSettingDao;


    @Override
    public void upload(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                Long count = orderSettingDao.findById(orderSetting.getOrderDate());
                if (count > 0) {
                    //已经存在
                    orderSettingDao.upload(orderSetting);
                } else {
                    //新添加
                    orderSettingDao.insert(orderSetting);
                }
            }
        }
    }
}
