package com.cheji.web;

import com.alibaba.fastjson.JSONObject;
import com.cheji.web.modular.service.AppAuctionService;
import com.cheji.web.modular.service.AppAuctionUpService;
import com.cheji.web.modular.service.AppAuctionWithdrawCashService;
import com.cheji.web.modular.service.AppAuctionWxPayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class CourseTest {
    @Resource
    private AppAuctionService ss;
    @Resource
    private AppAuctionUpService dd;
    @Resource
    private AppAuctionWithdrawCashService qq;

    @Resource
    private AppAuctionWxPayService aa;
    @Resource
    private AppAuctionService cc ;

    @Test
    public void test() throws Exception {
        JSONObject object = cc.auctionHot(null, null);
        System.out.println(object);
    }

    @Test
    public void test1() throws Exception {
    }
    public static void main(String[] args) {

    }

}