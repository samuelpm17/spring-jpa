package com.samuel.springjpa.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RequestMapping *)")
    public void allApiRequest() {
    }

    @Before("allApiRequest()")
    public void logging(JoinPoint joinPoint) {
        log.info("New request:");
        log.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        for (Object arg : joinPoint.getArgs()) {
            log.info("ARG: " + arg.toString());
        }
    }

    @AfterReturning(pointcut = "allApiRequest()", returning = "result")
    public void logging1(JoinPoint joinPoint, Object result) {
        log.info("Request finished succesfully");
        log.info("Return: " + result.toString());
    }

    @AfterThrowing(pointcut = "allApiRequest()", throwing = "exception")
    public void logging2(JoinPoint joinPoint, Exception exception) {
        log.info("Request finished on exception");
        log.info("Exception: " + exception.getMessage());
    }
}
