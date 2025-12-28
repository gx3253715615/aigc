package com.blockchain.aigc.dto;

import com.blockchain.aigc.enums.LicenseTypeEnum;
import com.blockchain.aigc.enums.TransferTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CopyrightTransferRequest {
    
    @NotBlank(message = "作品ID不能为空")
    private String workId;
    
    @NotNull(message = "受让方用户ID不能为空")
    private Long toUserId;
    
    @NotNull(message = "转让类型不能为空")
    private TransferTypeEnum transferType;
    
    private LicenseTypeEnum licenseType;
    
    private LocalDateTime effectiveTime;
    
    private LocalDateTime expireTime;
    
    private String tradeDesc;
    
    private BigDecimal price;
}
