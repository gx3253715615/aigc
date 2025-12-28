package com.blockchain.aigc.controller;

import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.LoginRequest;
import com.blockchain.aigc.dto.RegisterRequest;
import com.blockchain.aigc.dto.UserProfileDTO;
import com.blockchain.aigc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            Map<String, Object> result = userService.register(request);
            return ApiResponse.success("注册成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = userService.login(request);
            return ApiResponse.success("登录成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public ApiResponse<UserProfileDTO> getProfile() {
        try {
            UserProfileDTO profile = userService.getProfile();
            return ApiResponse.success(profile);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     * 注意：auth_status、status、wallet_address三个字段不允许用户更新
     */
    @PutMapping("/profile")
    public ApiResponse<String> updateProfile(@RequestBody UserProfileDTO request) {
        try {
            userService.updateProfile(request);
            return ApiResponse.success("更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
