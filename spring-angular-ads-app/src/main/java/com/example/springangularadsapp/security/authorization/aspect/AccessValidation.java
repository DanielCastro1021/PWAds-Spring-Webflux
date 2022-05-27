package com.example.springangularadsapp.security.authorization.aspect;

import com.example.springangularadsapp.security.models.ERole;
import com.example.springangularadsapp.security.exception.UnauthorizedAccessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Component
@Aspect
public class AccessValidation {

    private static final Logger logger = LoggerFactory.getLogger(AccessValidation.class);


    @Pointcut("@annotation(com.example.springangularadsapp.security.authorization.annotation.AdminAccess)")
    public void methodsWithAdminAccessAnnotation() {
    }

    @Pointcut("@annotation(com.example.springangularadsapp.security.authorization.annotation.ModeratorAccess)")
    public void methodsWithModeratorAccessAnnotation() {
    }

    @Pointcut("@annotation(com.example.springangularadsapp.security.authorization.annotation.UserAccess)")
    public void methodsWithUserAccessAnnotation() {
    }


    @Around(value = "methodsWithAdminAccessAnnotation()")
    public Object checkAdminPrivileges(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (!request.isUserInRole("ROLE_ADMIN")) throw new UnauthorizedAccessException(ERole.ROLE_ADMIN);
        else return proceedingJoinPoint.proceed();
    }

    @Around(value = "methodsWithModeratorAccessAnnotation()")
    public Object checkModeratorPrivileges(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (!request.isUserInRole("ROLE_MODERATOR")) throw new UnauthorizedAccessException(ERole.ROLE_MODERATOR);
        else return proceedingJoinPoint.proceed();
    }

    @Around(value = "methodsWithUserAccessAnnotation()")
    public Object checkUserPrivileges(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_MODERATOR") || request.isUserInRole("ROLE_ADMIN"))
            return proceedingJoinPoint.proceed();
        else throw new UnauthorizedAccessException(ERole.ROLE_USER);
    }
}
