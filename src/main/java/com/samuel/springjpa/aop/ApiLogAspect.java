package com.samuel.springjpa.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {
    
    @Before("@annotation(ApiLog)")
    public void logging(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
    }
}
