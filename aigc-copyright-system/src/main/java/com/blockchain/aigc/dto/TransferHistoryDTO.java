package com.blockchain.aigc.dto;

import com.blockchain.aigc.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferHistoryDTO {
    
    private Long id;
    private String transferId;
    private String workId;
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    private String fromAddress;
    private String toAddress;
    private TransferTypeEnum transferType;
    private String currentRightType;
    private LicenseTypeEnum licenseType;
    private LocalDateTime effectiveTime;
    private LocalDateTime expireTime;
    private String chainTxHash;
    private Long blockNumber;
    private LocalDateTime blockTime;
    private ChainStatusEnum chainStatus;
    private TransferStatusEnum transferStatus;
    private String tradeDesc;
    private BigDecimal price;
    private LocalDateTime createTime;
}
