package com.blockchain.aigc.controller;

import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.UploadWorkRequest;
import com.blockchain.aigc.dto.WorkDTO;
import com.blockchain.aigc.service.WorkService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/works")
@CrossOrigin
public class WorkController {
    
    @Autowired
    private WorkService workService;
    
    /**
     * 上传作品
     */
    @PostMapping("/upload")
    public ApiResponse<WorkDTO> uploadWork(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "summary", required = false) String summary,
            @RequestParam(value = "modelName", required = false) String modelName,
            @RequestParam(value = "modelVersion", required = false) String modelVersion,
            @RequestParam(value = "modelSource", required = false) String modelSource,
            @RequestParam(value = "modelParams", required = false) String modelParamsJson,
            @RequestParam(value = "prompt", required = false) String prompt,
            @RequestParam(value = "creationType", required = false) String creationType) {
        try {
            UploadWorkRequest request = new UploadWorkRequest();
            request.setFileName(file.getOriginalFilename());
            request.setSummary(summary);
            request.setModelName(modelName);
            request.setModelVersion(modelVersion);
            request.setModelSource(modelSource);
            request.setPrompt(prompt);
            request.setCreationType(creationType);
            
            // 解析模型参数JSON为Map<String, String>
            if (modelParamsJson != null && !modelParamsJson.isEmpty()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> modelParams = objectMapper.readValue(
                            modelParamsJson, 
                            new TypeReference<Map<String, String>>() {}
                    );
                    request.setModelParams(modelParams);
                } catch (Exception e) {
                    return ApiResponse.error("模型参数格式错误: " + e.getMessage());
                }
            }
            
            WorkDTO result = workService.uploadWork(file, request);
            return ApiResponse.success("上传成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取作品详情
     */
    @GetMapping("/{id}")
    public ApiResponse<WorkDTO> getWork(@PathVariable Long id) {
        try {
            WorkDTO work = workService.getWorkById(id);
            return ApiResponse.success(work);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取当前用户作品列表
     */
    @GetMapping("/my")
    public ApiResponse<Page<WorkDTO>> getMyWorks(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<WorkDTO> page = workService.getMyWorks(pageNum, pageSize);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 手动确权
     */
    @PostMapping("/{id}/certify")
    public ApiResponse<String> certifyWork(@PathVariable Long id) {
        try {
            WorkDTO workDTO = workService.getWorkById(id);
            workService.certifyWork(workDTO.getWorkId());
            return ApiResponse.success("确权成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
