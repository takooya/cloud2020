package com.atguigu.springcloud.controller;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@DefaultProperties(defaultFallback = "paymentInfo_default_fallback", commandProperties = {
        @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "5000")
})
@Slf4j
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_Timeout(id);
    }

    @GetMapping("/consumer/payment/hystrix/try_time/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_try_time_handler", commandProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "5000")
    })
    public String paymentInfo_try_time(@PathVariable("id") Integer id) {
        try {
            TimeUnit.SECONDS.sleep(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "我等待了：" + id + "秒";
    }

    public String paymentInfo_try_time_handler(@PathVariable("id") Integer id) {
        return "OrderHystrixController.paymentInfo_TimeoutHandler";
    }


    @GetMapping("/consumer/payment/hystrix/paymentInfo_default/{id}")
    @HystrixCommand
    public String paymentInfo_default(@PathVariable("id") Integer id) {
        try {
            TimeUnit.SECONDS.sleep(id);
            if (id > 5) {
                int age = 10 / 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "我等待了：" + id + "秒";
    }

    public String paymentInfo_default_fallback() {
        return "OrderHystrixController.paymentInfo_default_fallback";
    }


    // ========================== 服务熔断 ========================== //

    /**
     * {@link com.netflix.hystrix.HystrixCommandProperties}
     */
    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback", commandProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "10000"),
//            是否开启断路器
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ENABLED, value = "true"),
//            请求次数
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "10"),
//            时间窗口期/时间范围
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, value = "10000"),
//            失败率
            @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "60")
    })
    @GetMapping("/consumer/payment/hystrix/payment_circuit_breaker/{id}")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("id不可以是负数！！！");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t调用成功，流水号：" + serialNumber;
    }

    public String paymentCircuitBreakerFallback(@PathVariable("id") Integer id) {
        return "OrderHystrixController.paymentCircuitBreakerFallback!!!";
    }
}
