package com.portrait.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portrait.common.Result;
import com.portrait.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error(401, "未登录，请先登录")));
            return false;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = JwtUtil.parseToken(token);
            request.setAttribute("userId", claims.get("userId", Long.class));
            request.setAttribute("workNo", claims.get("workNo", String.class));
            request.setAttribute("role", claims.get("role", String.class));
            request.setAttribute("name", claims.get("name", String.class));
            return true;
        } catch (Exception e) {
            log.warn("JWT校验失败: {}", e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error(401, "Token无效或已过期")));
            return false;
        }
    }
}