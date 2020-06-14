package com.atguigu.springcloud.controller;

import java.util.concurrent.TimeUnit;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

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

    @GetMapping(value = "/payment/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        String serviceInfo = String.join(",", services);
        log.info("[-PaymentController-].discovery:serviceInfo={}", serviceInfo);
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        String instancesInfo = instances.stream()
                .map(si -> si.getServiceId() + "\t" + si.getHost() + "\t" + si.getPort() + "\t" + si.getUri())
                .collect(Collectors.joining(","));
        log.info("[-PaymentController-].discovery:instancesInfo={}", instancesInfo);
        return discoveryClient;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    @GetMapping(value = "/payment/lb")
    public String lb() {
        return serverPort;
    }
}
