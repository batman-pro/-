package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByPhone(String telephone) {
        return  memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            member.setPassword(MD5Utils.md5(password));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findAllByTime(List<String> timeList) {
        List<Integer> list = new ArrayList<>();
        for (String time : timeList) {
            time = time + ".31";
            Integer countBeforeDate = memberDao.findMemberCountBeforeDate(time);
            list.add(countBeforeDate);
        }
        return list;
    }


}
