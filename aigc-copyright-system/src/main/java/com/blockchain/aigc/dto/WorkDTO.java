package com.blockchain.aigc.dto;

import com.blockchain.aigc.enums.FileTypeEnum;
import com.blockchain.aigc.enums.LicenseTypeEnum;
import com.blockchain.aigc.enums.RightTypeEnum;
import com.blockchain.aigc.enums.WorkStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class WorkDTO {
    
    private Long id;
    private String workId;
    private Long userId;
    private String userName;
    private String fileName;
    private FileTypeEnum fileType;
    private Long fileSize;
    private String fileHash;
    private String summary;
    private String modelName;
    private String modelVersion;
    private String modelSource;
    private Map<String, Object> modelParams;
    private String prompt;
    private String creationType;
    private WorkStatusEnum workStatus;
    private RightTypeEnum rightType;
    private LicenseTypeEnum licenseType;
    private String chainTxHash;
    private Long blockNumber;
    private String fileUrl;
    private LocalDateTime createTime;
}
