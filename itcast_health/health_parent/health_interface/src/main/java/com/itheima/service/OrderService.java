package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


public interface OrderService{
    Result order(Map map) throws Exception;

    Map findById(int id) throws Exception;

}
