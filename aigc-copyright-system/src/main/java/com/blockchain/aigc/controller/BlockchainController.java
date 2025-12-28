package com.blockchain.aigc.controller;

import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
@CrossOrigin
public class BlockchainController {
    
    @Autowired
    private BlockchainService blockchainService;
    
    /**
     * 查询版权证书
     */
    @GetMapping("/certificate/{workId}")
    public ApiResponse<Map<String, Object>> getCertificate(@PathVariable String workId) {
        try {
            Map<String, Object> certificate = blockchainService.getCertificate(workId);
            return ApiResponse.success(certificate);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 验证版权
     */
    @GetMapping("/verify")
    public ApiResponse<Boolean> verifyCopyright(
            @RequestParam String workId,
            @RequestParam String fileHash) {
        try {
            boolean isValid = blockchainService.verifyCopyright(workId, fileHash);
            return ApiResponse.success(isValid);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 查询链上转让历史
     */
    @GetMapping("/transfer-history/{workId}")
    public ApiResponse<Object[]> getWorkTransferHistory(@PathVariable String workId) {
        try {
            Object[] history = blockchainService.getWorkTransferHistory(workId);
            return ApiResponse.success(history);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
