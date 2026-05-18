package com.hwscs.backend.logger.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(*com.hwscs.backend.service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering: {} | Args: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.hwscs.backend.service..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Exiting: {} | Result: {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.hwscs.backend.service..*(..))", throwing = "ex")
    public void logError(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in {} | Message: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }

}