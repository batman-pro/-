package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkgroupId = checkGroup.getId();
        this.setGI(checkgroupId,checkitemIds);
    }

    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> checkGroups = checkGroupDao.findpage(queryPageBean.getQueryString());
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }

    @Override
    public CheckGroup findbyid(Integer id) {
        return checkGroupDao.findbyid(id);
    }

    @Override
    public List<Integer> fbgroupiditem(Integer id) {
        List<Integer> list = checkGroupDao.fbgroupiditem(id);
        return list;
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //编辑检查组
        checkGroupDao.editgroupone(checkGroup);
        //删除后添加'组'与'项'关系
        // 删除
        checkGroupDao.deleteGI(checkGroup.getId());
        // 添加
        Integer checkGroupId = checkGroup.getId();
        this.setGI(checkGroupId,checkitemIds);
    }

    @Override
    public void delete(Integer id) {
        //删除'组'关联'项'
        checkGroupDao.deleteGI(id);
        //删除检查组
        checkGroupDao.deleteG(id);

    }

    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> list = checkGroupDao.findAll();
        return list;
    }

    public void setGI(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            Map<String,Integer> map = null;
            for (Integer checkitemId : checkitemIds) {
                map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setGroupItem(map);
            }
        }

    }
}
