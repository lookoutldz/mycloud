package org.looko.mycloud.user.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.looko.mycloud.commonstarter.entity.enumeration.ResponseStatusEnum.BUSINESS_ERROR;

@Slf4j
@Aspect
@Component
public class ControllerAdvice {

    @Pointcut("execution(* org.looko.mycloud.user.controller.*.*(..))")
    public void universal() {}

    @Around("universal()")
    public ResponseEntity<?> responseWrapping(ProceedingJoinPoint joinPoint) {
        try {
            return (ResponseEntity<?>) joinPoint.proceed();
        } catch (Throwable e) {
            log.error("发生了错误：", e);
            return ResponseEntity.failure(BUSINESS_ERROR, e.getMessage());
        }
    }
}
