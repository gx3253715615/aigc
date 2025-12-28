package com.blockchain.aigc.controller;

import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.CopyrightTransferRequest;
import com.blockchain.aigc.dto.TransferHistoryDTO;
import com.blockchain.aigc.service.CopyrightService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/copyright")
@CrossOrigin
public class CopyrightController {
    
    @Autowired
    private CopyrightService copyrightService;
    
    /**
     * 版权转让
     */
    @PostMapping("/transfer")
    public ApiResponse<TransferHistoryDTO> transferCopyright(
            @Valid @RequestBody CopyrightTransferRequest request) {
        try {
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
}
