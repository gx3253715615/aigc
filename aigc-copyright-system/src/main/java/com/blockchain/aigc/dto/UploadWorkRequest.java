package com.blockchain.aigc.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class UploadWorkRequest {
    
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    
    private String summary;
    
    private String modelName;
    
    private String modelVersion;
    
    private String modelSource;
    
    // 模型参数，简单的键值对
    private Map<String, String> modelParams;
    
    private String prompt;
    
    private String creationType;
}
