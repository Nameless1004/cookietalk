package com.sparta.cookietalk.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "ExecutionTime")
@Aspect
@Component
public class ExecutionTimeAop {
    @Pointcut("execution(* com.sparta.cookietalk.upload.UploadController.uploadVideo(..))")
    private void pc(){}

    @Around("pc()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("{}수행 시간: {}ms",joinPoint.getSignature().getName(), endTime - startTime);
            return result;
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
