package com.itheima.test;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.utils.SMSUtils;
import org.junit.Test;

public class duanxinTest {

    @Test
    public void test() throws ClientException {
        SMSUtils.sendShortMessage("SMS_200179856","15069859233","2020-08-29");
    }
}
