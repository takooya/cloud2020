package com.atguigu.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() +
                "\npaymentInfo_OK.id：" + id +
                "\n(￣▽￣)";
    }

    // "execution.isolation.thread.timeoutInMilliseconds"
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "8000")
    })
    public String paymentInfo_Timeout(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(id);
            if (id > 5) {
                int age = 10 / 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() +
                "\npaymentInfo_Timeout.id：" + id +
                "\n(￣▽￣)\t耗时" + id + "秒";
    }

    public String paymentInfo_TimeoutHandler(Integer id) {
        return "provider线程池：" + Thread.currentThread().getName() +
                "\npaymentInfo_TimeoutHandler.id：" + id +
                "\nd=====(￣▽￣*)b\t";
    }
}
