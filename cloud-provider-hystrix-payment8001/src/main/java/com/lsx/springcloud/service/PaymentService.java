package com.lsx.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    //服务降级
    /**
     * 正常访问
     * @param id
     * @return
     */
    public String paymentInfo_ok(Integer id){
        return "线程池"+Thread.currentThread().getName()+"pymentInfo_OK,id"+id+"\t"+"O(∩_∩)O哈哈~";
    }
    /**
     * 超时
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String paymentInfo_Timeout(Integer id){

        int timeNumber = 3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("服务端service被调用");
        return "线程池"+Thread.currentThread().getName()+"paymentInfo_Timeout,id"+id+"\t"+"O(∩_∩)O哈哈~耗时:"+timeNumber;
    }
    public String paymentInfo_TimeoutHandler(Integer id){

        return "线程池"+Thread.currentThread().getName()+"系统忙，请稍后再试,id"+id+"\t"+"难过(ಥ﹏ಥ)";
    }
    //服务熔断
    @HystrixCommand(fallbackMethod = "paymentCicruitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")//失败率达到多少后跳闸
    })
    public String paymentCiruitBreaker(@PathVariable("id")Integer id){
        if (id<0){
            throw new RuntimeException("******id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName()+"\t"+"调动成功，流水号:"+ serialNumber;
    }

    public String paymentCicruitBreaker_fallback(@PathVariable("id") Integer id){
        return "id不能为负数，请稍后再试，(ಥ﹏ಥ) id:"+id;
    }
}
