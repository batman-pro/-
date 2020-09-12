package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    //清除垃圾照片
    public void clearImg() {
        Set<String> imgs = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (imgs != null) {
            for (String img : imgs) {
                QiniuUtils.deleteFileFromQiniu(img);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
                System.out.println("清除垃圾图片成功!");
            }
        }
    }

    //清除缓存
    public void clearAllImg() {
        this.clearImg();
        Set<String> smembers = jedisPool.getResource().smembers(RedisConstant.SETMEAL_PIC_RESOURCES);
        if (smembers != null) {
            for (String smember : smembers) {
                QiniuUtils.deleteFileFromQiniu(smember);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,smember);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,smember);
            }
        }
    }

}
