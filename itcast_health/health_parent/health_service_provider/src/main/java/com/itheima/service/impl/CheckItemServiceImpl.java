package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findpage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.findpage(queryString);
        long total = page.getTotal();
        List<CheckItem> result = page.getResult();
        PageResult pageResult = new PageResult(total,result);
        return pageResult;
    }

    @Override
    public void delete(Integer id) {
        //判断'项'是否在'组'里
        long num = checkItemDao.findGroupAnditem(id);
        if (num > 0) {
            //不可删除
            throw new RuntimeException("该检查项在检查组里,无法删除");
        } else {
            //可以删除
            checkItemDao.deleteById(id);
        }
    }

    @Override
    public CheckItem findbyid(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> list = checkItemDao.findAll();
        return list;
    }
}
