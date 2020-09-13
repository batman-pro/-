package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.AddPhone;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            if (strings != null && strings.size() > 0) {
                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] string : strings) {
                    OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.upload(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }


    @PostMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean page) {
        PageResult pageResult = orderSettingService.findpage(page);
        return pageResult;
    }

    @GetMapping("/findadd")
    public Result findadd() {
        try {
            List<Setmeal> list = orderSettingService.findadd();
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @PostMapping("/add")
    public Result add(Integer[] setmealIds, @RequestBody AddPhone addPhone) {
        try {
            if (setmealIds == null) {
                throw new NullPointerException("没有选择套餐");
            }
            orderSettingService.add(setmealIds, addPhone);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "电话预约添加失败");
        }
        return new Result(true, "电话预约添加成功");
    }
}
