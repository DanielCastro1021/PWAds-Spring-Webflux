package com.example.springangularadsapp.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
public class AuditAspect {

    private final StatsLogRepository repository;

    public AuditAspect(StatsLogRepository repository) {
        this.repository = repository;
    }

    @Pointcut("execution(* com.example.springangularadsapp.components..*..controller..*.*(..))")
    void allMethodsInAComponentsControllerPackage() {
    }

    @Around(value = "allMethodsInAComponentsControllerPackage()")
    public Object endpointAudit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        String requestId = UUID.randomUUID().toString();
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String apiName = className + "." + methodName;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        StatsLog statsLog = new StatsLog(requestId, request.getHeader("host"), request.getMethod(), request.getRequestURI(), apiName, Arrays.toString(proceedingJoinPoint.getArgs()), elapsedTime);
        this.repository.save(statsLog);

        return result;
    }
}
