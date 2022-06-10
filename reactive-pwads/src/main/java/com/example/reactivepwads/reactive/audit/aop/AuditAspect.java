package com.example.reactivepwads.reactive.audit.aop;

import com.example.reactivepwads.reactive.audit.model.AuditLog;
import com.example.reactivepwads.reactive.audit.repository.AuditLogReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@AllArgsConstructor
@Log
public class AuditAspect {
    private final AuditLogReactiveRepository repository;

    @Pointcut("execution(* com.example.reactivepwads.reactive..*..handler..*.*(..))")
    void allMethodsInAComponentsHandlerPackage() {
    }

    @Around(value = "allMethodsInAComponentsHandlerPackage()")
    public Object saveAuditLog(ProceedingJoinPoint proceedingJoinPoint) {
        final long start = System.currentTimeMillis();
        Mono proceed = null;
        try {
            proceed = (Mono) proceedingJoinPoint.proceed();
            return proceed.doOnNext(response -> {
                final long end = System.currentTimeMillis();
                List<String> argList = parseRequestArgs(proceedingJoinPoint.getArgs());
                String request_protocol = argList.get(0);
                String http_method = argList.get(1);
                String uri = argList.get(2);
                String api_class = proceedingJoinPoint.getSignature().getDeclaringTypeName();
                String api_method = proceedingJoinPoint.getSignature().getName();
                String api_full_name = api_class + "." + api_method;
                long elapsedTime = end - start;
                Mono.just(new AuditLog(request_protocol, http_method, uri, api_class, api_method, api_full_name, elapsedTime)).flatMap(repository::save).subscribe();

            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }

    private List<String> parseRequestArgs(Object[] args) {
        String argsStr = Arrays.toString(args);
        List<String> argList = new ArrayList<>();
        for (String arg : argsStr.split(" ")) {
            argList.add(arg.replaceAll("\\[", "").replaceAll("\\]", ""));
        }
        return argList;
    }
}
