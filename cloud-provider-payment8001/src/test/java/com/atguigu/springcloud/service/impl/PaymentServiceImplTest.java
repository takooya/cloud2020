package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.PaymentMain8001;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentMain8001.class})
class PaymentServiceImplTest {
    @Resource
    private PaymentService paymentService;

    @Test
    void create() {
        int testResult = paymentService.create(new Payment(1L, "测试使用"));
        Assert.assertEquals(1, testResult);
    }

    @Test
    void getPaymentById() {
        Payment paymentById = paymentService.getPaymentById(1L);
        log.info("[-PaymentServiceImplTest-].getPaymentById:={}", paymentById);
    }
}