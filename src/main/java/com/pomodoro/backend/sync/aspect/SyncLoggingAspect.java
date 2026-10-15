package com.pomodoro.backend.sync.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class SyncLoggingAspect {
    private final Logger logger = Logger.getLogger(SyncLoggingAspect.class.getName());

    @Around("execution(* com.pomodoro.backend.sync.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint)
            throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        logger.info("Method " + methodName +
                " with parameters " + Arrays.asList(arguments) +
                " will execute");

        Object returnedByMethod = joinPoint.proceed();
        logger.info("Method executed and returned " + returnedByMethod);
        return returnedByMethod;
    }
}