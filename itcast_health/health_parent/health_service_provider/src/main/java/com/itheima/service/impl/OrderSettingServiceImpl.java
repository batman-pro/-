package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.PhoneMemeberConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSSDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.AddPhone;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.OrderSettinglist;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSSDao orderSettingDao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

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

    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<OrderSettinglist> page = orderSettingDao.findpage(queryPageBean.getQueryString());
        List<OrderSettinglist> result = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,result);
    }

    @Override
    public List<Setmeal> findadd() {
        return setmealDao.findAll();
    }

    @Override
    public void add(Integer[] setmealIds, AddPhone addPhone) {
        //查询并存储电话会员
        AddPhone phoneMember = memberDao.findByTelephoneandPhone(addPhone.getPhoneNumber());
        if (phoneMember == null) {
            //开始存储
            memberDao.addPhoneMember(addPhone);
            Integer id = addPhone.getId();
            add_two(addPhone,setmealIds,id);
        } else {
            Integer id = memberDao.findIdByPhone(addPhone.getPhoneNumber());
            add_two(addPhone,setmealIds,id);
        }
    }
    public void add_two(AddPhone addPhone, Integer[] setmealIds, Integer id) {
        Map<String, Object> map = new HashMap<>();
        Date orderDate = addPhone.getOrderDate();
        map.put("orderDate",orderDate);
        map.put("orderType", PhoneMemeberConstant.ORDER_TYPE);
        map.put("orderStatus",PhoneMemeberConstant.ORDER_STATUS);
        map.put("addphone_id",id);
        map.put("setmeal_id",setmealIds[0]);
        orderDao.addByPhone(map);
    }
}
