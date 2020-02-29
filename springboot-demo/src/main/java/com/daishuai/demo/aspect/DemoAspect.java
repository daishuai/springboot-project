package com.daishuai.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.daishuai.demo.dto.WaterAllocationResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Daishuai
 * @date 2020/2/20 15:48
 */
@Aspect
@Component
public class DemoAspect {

    @Pointcut(value = "execution(com.daishuai.demo.dto.WaterAllocationResponse com.daishuai.demo.DemoApplication.*(..))")
    public void pointCut() {}


    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        //System.out.println(JSON.toJSONString(joinPoint));
    }

    @AfterReturning(value = "pointCut()", returning = "response")
    public void afterReturning(JoinPoint joinPoint, WaterAllocationResponse response) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String name = method.getName();
        System.out.println(name);
        Object[] args = joinPoint.getArgs();
        System.out.println(JSON.toJSONString(args));
        System.out.println(JSON.toJSONString(response));
    }
}
