package com.blockchain.aigc.controller;

import com.blockchain.aigc.annotation.Log;
import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.CopyrightTransferRequest;
import com.blockchain.aigc.dto.TransferHistoryDTO;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.enums.OperationTypeEnum;
import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.service.CopyrightService;
import com.blockchain.aigc.utils.UserUtil;
import com.mybatisflex.core.paginate.Page;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/copyright")
@CrossOrigin
public class CopyrightController {

    @Autowired
    private CopyrightService copyrightService;

    /**
     * 版权转让
     */
    @Log(module = "版权转让", operationType = OperationTypeEnum.CREATE, description = "转让版权", targetType = "work")
    @PostMapping("/transfer")
    public ApiResponse<TransferHistoryDTO> transferCopyright(
            @Valid @RequestBody CopyrightTransferRequest request) {
        try {
            // 检查用户认证状态
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return ApiResponse.error("用户未登录");
            }

            TransferHistoryDTO result = copyrightService.transferCopyright(request);
            return ApiResponse.success("转让成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询转让历史
     */
    @GetMapping("/transfers")
    @Deprecated
    public ApiResponse<Page<TransferHistoryDTO>> getTransferHistory(
            @RequestParam String workId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<TransferHistoryDTO> page = copyrightService.getTransferHistory(workId, pageNum, pageSize);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/transfers/my")
    public ApiResponse<Page<TransferHistoryDTO>> getMyTransferHistory(
            @RequestParam(defaultValue = "ALL") String direction,
            @RequestParam(required = false) String workId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return ApiResponse.error("用户未登录");
            }
            Page<TransferHistoryDTO> page = copyrightService.getMyTransferHistory(currentUser.getId(), direction, workId, pageNum, pageSize);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取转让详情
     */
    @GetMapping("/transfers/{id}")
    public ApiResponse<TransferHistoryDTO> getTransferById(@PathVariable Long id) {
        try {
            TransferHistoryDTO transfer = copyrightService.getTransferById(id);
            return ApiResponse.success(transfer);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询区块链转让详情
     */
    @GetMapping("/blockchain/transfers/{id}")
    public ApiResponse<Map<String, Object>> getTransferById(@PathVariable("id") String transferId) {
        try {
            Map<String, Object> blockInfo = copyrightService.getBlockInfoByTransferId(transferId);
            return ApiResponse.success(blockInfo);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询区块链转让详情
     */
    @GetMapping("/blockchain/transfersHistory/{id}")
    public ApiResponse<List<String>> getTransferHistoryByWorkId(@PathVariable("id") String workId) {
        try {
            List<String> transferIds = copyrightService.getTransferHistoryByWorkId(workId);
            return ApiResponse.success(transferIds);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
