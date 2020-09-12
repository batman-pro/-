package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //进行预约提交
        //用户输入验证码验证
        String telephone = (String) map.get("telephone");
        String redis_code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        Result result = null;
        if (validateCode != null && redis_code != null && redis_code.equals(validateCode)) {
            //验证成功
            try {
                map.put("orderType", Order.ORDERTYPE_WEIXIN);
                result = orderService.order(map);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result = new Result(false, "存在一个未知的错误");
                return result;
            }
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //待完成
//        if (result.isFlag()) {
//            //预约成功,返回成功信息
//            SMSUtils.sendShortMessage();
//
//        }
    }

    @GetMapping("/findById")
    public Result findById(int id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
