package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult<Integer> create(@RequestBody Payment payment) {
        log.info("[-PaymentController.{}-].create:={}", serverPort, payment);
        int result = paymentService.create(payment);
        log.info("[-PaymentController.{}-].create:={}", serverPort, result);
        if (result > 0) {
            return new CommonResult<>(200, "插入数据库成功(" + serverPort + ")", result);
        } else {
            return new CommonResult<>(444, "插入数据库失败(" + serverPort + ")");
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        Payment result = paymentService.getPaymentById(id);
        log.info("[-PaymentController.{}-].getPaymentById:={}", serverPort, result);
        ;
        if (result != null) {
            return new CommonResult<>(200, "查询成功(" + serverPort + ")", result);
        } else {
            return new CommonResult<>(444, "没有对应记录，查询id：(" + serverPort + ")" + id);
        }
    }
}
