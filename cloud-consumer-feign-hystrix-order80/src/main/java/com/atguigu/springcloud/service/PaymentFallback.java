package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallback implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "PaymentFallback.paymentInfo_OK id:" + id;
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "PaymentFallback.paymentInfo_Timeout id:" + id;
    }
}
