package com.blockchain.aigc.controller;

import com.blockchain.aigc.annotation.Log;
import com.blockchain.aigc.dto.AdminUserDTO;
import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.RegisterRequest;
import com.blockchain.aigc.enums.OperationTypeEnum;
import com.blockchain.aigc.enums.UserStatusEnum;
import com.blockchain.aigc.service.UserService;
import com.mybatisflex.core.paginate.Page;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Log(module = "用户", operationType = OperationTypeEnum.REGISTER, description = "新增用户", targetType = "user")
    public ApiResponse<Map<String, Object>> createUser(@Valid @RequestBody RegisterRequest request) {
        try {
            Map<String, Object> result = userService.register(request);
            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<Page<AdminUserDTO>> listUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        try {
            Page<AdminUserDTO> page = userService.getAdminUserList(pageNum, pageSize, keyword);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Log(module = "用户", operationType = OperationTypeEnum.UPDATE, description = "更新用户状态", targetType = "user")
    @PutMapping("/{id}/status")
    public ApiResponse<String> updateStatus(@PathVariable Long id, @RequestBody UpdateUserStatusRequest request) {
        try {
            if (request == null || request.getStatus() == null) {
                return ApiResponse.error("status 不能为空");
            }
            userService.updateUserStatusByAdmin(id, request.getStatus());
            return ApiResponse.success("更新成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Data
    public static class UpdateUserStatusRequest {
        private UserStatusEnum status;
    }
}
