package com.portrait.aspect;

import com.portrait.annotation.RequireRole;
import com.portrait.common.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(com.portrait.annotation.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException(401, "未登录");
        }

        HttpServletRequest request = attributes.getRequest();
        String currentRole = (String) request.getAttribute("role");
        if (currentRole == null) {
            throw new BusinessException(401, "未登录");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequireRole requireRole = signature.getMethod().getAnnotation(RequireRole.class);
        String[] allowedRoles = requireRole.value();

        if (Arrays.stream(allowedRoles).noneMatch(r -> r.equals(currentRole))) {
            throw new BusinessException(403, "权限不足");
        }
    }
}