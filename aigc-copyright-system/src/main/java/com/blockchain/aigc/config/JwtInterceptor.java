package com.blockchain.aigc.config;

import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.service.UserService;
import com.blockchain.aigc.utils.JwtUtil;
import com.blockchain.aigc.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器 - 自动设置当前用户到ThreadLocal
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

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
