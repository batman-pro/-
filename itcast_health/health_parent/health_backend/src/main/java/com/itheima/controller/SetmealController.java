package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        int indexOf = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(indexOf);
        String filename = UUID.randomUUID().toString() + substring;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);
            //添加到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findpage")
    public PageResult findpage(@RequestBody QueryPageBean page) {
        PageResult pageResult = setmealService.findpage(page);
        return new PageResult(pageResult.getTotal(), pageResult.getRows());
    }

    @GetMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true,"删除套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除套餐失败");
        }
    }
}
