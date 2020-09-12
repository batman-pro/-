package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findByPhone(String telephone);

    void add(Member member);

    List<Integer> findAllByTime(List<String> timeList);

}
