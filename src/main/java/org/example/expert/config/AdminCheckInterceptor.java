package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
        UserRole role = (UserRole) request.getAttribute("userRole"); // 수정

        if(role != UserRole.ADMIN) {
            log.warn("(interceptor)관리자 권한 없음: role={}, URI={}", role, request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        log.info("ADMIN 요청: URI={}, time={}", request.getRequestURI(), System.currentTimeMillis());
        return true;
    }
}
