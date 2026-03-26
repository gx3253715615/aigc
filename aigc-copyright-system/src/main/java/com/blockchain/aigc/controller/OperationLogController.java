package com.blockchain.aigc.controller;

import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.entity.OperationLog;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.enums.ResultEnum;
import com.blockchain.aigc.handler.exception.GlobalException;
import com.blockchain.aigc.service.OperationLogService;
import com.blockchain.aigc.utils.UserUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.blockchain.aigc.entity.table.OperationLogTableDef.OPERATION_LOG;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询操作日志列表
     * 管理员可以查看所有，普通用户只能查看自己的
     */
    @GetMapping("/list")
    public ApiResponse<Page<OperationLog>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword) {
        try {
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                throw new GlobalException(ResultEnum.NO_LOGIN);
            }

            QueryWrapper queryWrapper = QueryWrapper.create()
                    .orderBy(OPERATION_LOG.CREATE_TIME.desc());

            // 如果不是管理员，只能看自己的日志，且忽略传入的 username 参数
            if (currentUser.getIsAdmin() == null || currentUser.getIsAdmin() != 1) {
                queryWrapper.where(OPERATION_LOG.USER_ID.eq(currentUser.getId()));
            } else {
                // 管理员可以根据用户名查询
                if (username != null && !username.trim().isEmpty()) {
                    queryWrapper.and(OPERATION_LOG.USERNAME.like("%" + username.trim() + "%"));
                }
            }

            // 模块筛选
            if (module != null && !module.trim().isEmpty()) {
                queryWrapper.and(OPERATION_LOG.MODULE.eq(module.trim()));
            }

            // 关键词模糊查询描述
            if (keyword != null && !keyword.trim().isEmpty()) {
                queryWrapper.and(OPERATION_LOG.DESCRIPTION.like("%" + keyword.trim() + "%"));
            }

            Page<OperationLog> page = operationLogService.page(new Page<>(pageNum, pageSize), queryWrapper);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
