package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String out_put_path;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加表单
        setmealDao.addForm(setmeal);
        //添加'套餐'和'组'关系
        Integer setmealId = setmeal.getId();
        setSetmealGroup(checkgroupIds,setmealId);

        //添加到redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());

        generateMobileStaticHtml();
    }

    private void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);

    }

    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        Map<String,Object> map = new HashMap<>();
        map.put("setmealList",setmealList);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
//        Map<String,Object> map = null;
        for (Setmeal setmeal : setmealList) {
            Map<String,Object> map = new HashMap<>();
            map.put("setmeal",findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    map);
        }
    }

    //最原始创造
    public void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap) {
        Configuration configuration =
                freeMarkerConfigurer.getConfiguration();
        Writer writer = null;
        try {
            Template template = configuration.getTemplate(templateName);
            writer = new FileWriter(new File(out_put_path + "\\" + htmlPageName));
            template.process(dataMap,writer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public PageResult findpage(QueryPageBean page) {
        PageHelper.startPage(page.getCurrentPage(),page.getPageSize());
        Page<Setmeal> pageres = setmealDao.findpage(page.getQueryString());
        return new PageResult(pageres.getTotal(),pageres.getResult());
    }

    @Override
    public void delete(Integer id) {
        //先删
        setmealDao.deleteSG(id);
        //后删
        setmealDao.delete(id);

        generateMobileStaticHtml();

    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = setmealDao.findAll();
        return list;
    }

    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findCount() {
        return setmealDao.findCount();
    }

    public void setSetmealGroup(Integer[] checkgroupIds, int setmealId) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            Map<String,Integer> map = null;
            for (Integer checkgroupId : checkgroupIds) {
                map = new HashMap<>();
                map.put("checkgroupId",checkgroupId);
                map.put("setmealId",setmealId);
                setmealDao.addSG(map);
            }
        }
    }
}
