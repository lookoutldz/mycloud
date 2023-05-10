package org.looko.mycloud.commonstarter.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;

import static org.looko.mycloud.commonstarter.enumeration.ResponseStatusEnum.BUSINESS_ERROR;

@Slf4j
@Aspect
public class GlobalControllerAdvice {
    @Pointcut("execution(* org.looko.mycloud.*.controller.*.*(..))")
    public void universal() {}

    @Pointcut("@annotation(org.looko.mycloud.commonstarter.annotation.NoResponseWrapping)")
    public void noResponseWrappingAnnotated() {}

    @Around("universal() && !noResponseWrappingAnnotated()")
    public ResponseEntity<?> responseWrapping(ProceedingJoinPoint joinPoint) {
        try {
            return (ResponseEntity<?>) joinPoint.proceed();
        } catch (Throwable e) {
            log.error("发生了错误：", e);
            return ResponseEntity.failure(BUSINESS_ERROR, e.getMessage());
        }
    }
}
