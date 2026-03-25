package com.blockchain.aigc.config;

import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.enums.UserStatusEnum;
import com.blockchain.aigc.service.UserService;
import com.blockchain.aigc.utils.JwtUtil;
import com.blockchain.aigc.utils.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * JWT拦截器 - 自动设置当前用户到ThreadLocal
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwtToken = token.replace("Bearer ", "");
                if (jwtUtil.validateToken(jwtToken)) {
                    Long userId = jwtUtil.getUserIdFromToken(jwtToken);
                    User user = userService.getUserById(userId);
                    if (user != null) {
                        if (user.getStatus() == UserStatusEnum.DISABLE) {
                            response.setStatus(401);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            response.setContentType("application/json;charset=UTF-8");
                            String msg = userService.buildDisabledMessage();
                            response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.error(401, msg)));
                            return false;
                        }
                        UserUtil.set(user);
                    }
                }
            } catch (Exception e) {
                // Token无效，不设置用户
            }
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清理ThreadLocal
        UserUtil.remove();
    }
}
