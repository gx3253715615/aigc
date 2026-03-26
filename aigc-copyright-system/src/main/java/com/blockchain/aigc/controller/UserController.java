package com.blockchain.aigc.controller;

import com.blockchain.aigc.annotation.Log;
import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.LoginRequest;
import com.blockchain.aigc.dto.RealnameAuthRequest;
import com.blockchain.aigc.dto.RegisterRequest;
import com.blockchain.aigc.dto.UserLookupDTO;
import com.blockchain.aigc.dto.UserProfileDTO;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.UserRealname;
import com.blockchain.aigc.enums.OperationTypeEnum;
import com.blockchain.aigc.service.UserService;
import com.blockchain.aigc.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Log(module = "用户管理", operationType = OperationTypeEnum.REGISTER, description = "用户注册", targetType = "user")
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
    @Log(module = "用户管理", operationType = OperationTypeEnum.LOGIN, description = "用户登录", targetType = "user")
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
    @Log(module = "用户管理", operationType = OperationTypeEnum.UPDATE, description = "更新用户信息", targetType = "user")
    @PutMapping("/profile")
    public ApiResponse<String> updateProfile(@RequestBody UserProfileDTO request) {
        try {
            userService.updateProfile(request);
            return ApiResponse.success("更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 上传头像
     */
    @Log(module = "用户管理", operationType = OperationTypeEnum.UPDATE, description = "上传头像", targetType = "user")
    @PostMapping("/avatar")
    public ApiResponse<Map<String, Object>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = userService.uploadAvatar(file);
            return ApiResponse.success("上传成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 实名认证
     */
    @Log(module = "用户管理", operationType = OperationTypeEnum.AUTH, description = "实名认证", targetType = "user")
    @PostMapping("/realname-auth")
    public ApiResponse<String> realnameAuth(@RequestBody RealnameAuthRequest request) {
        try {
            userService.realnameAuth(request.getRealName(), request.getIdNumber());
            return ApiResponse.success("实名认证申请已提交，请等待审核", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取实名认证信息
     */
    @GetMapping("/realname-auth")
    public ApiResponse<UserRealname> getRealnameAuth() {
        try {
            UserRealname auth = userService.getRealnameAuth();
            return ApiResponse.success(auth);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/lookup")
    public ApiResponse<UserLookupDTO> lookupUser(@RequestParam String keyword) {
        try {
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return ApiResponse.error("用户未登录");
            }

            UserLookupDTO dto = userService.lookupByPhoneOrEmail(keyword);
            if (dto == null) {
                return ApiResponse.success("未找到对应用户", null);
            }
            return ApiResponse.success(dto);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
