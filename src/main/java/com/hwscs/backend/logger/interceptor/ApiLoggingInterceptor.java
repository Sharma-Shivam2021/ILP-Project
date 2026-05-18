package com.hwscs.backend.logger.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger =
            LoggerFactory.getLogger(ApiLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {

        logger.info("Request: {} {} | IP: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                @NotNull Object handler,
                                Exception ex) {

        logger.info("Response: {} {} | Status: {}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus());
    }
}