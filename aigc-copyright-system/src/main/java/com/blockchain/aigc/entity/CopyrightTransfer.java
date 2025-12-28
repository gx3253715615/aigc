package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.*;
import com.blockchain.aigc.handler.listener.MyInsertListener;
import com.blockchain.aigc.handler.listener.MyUpdateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Table(value = "copyright_transfers", onInsert = MyInsertListener.class, onUpdate = MyUpdateListener.class)
public class CopyrightTransfer extends Base {

    // 交易幂等号
    @Column(value = "transfer_id")
    private String transferId;

    // 关联作品表
    @Column(value = "work_id")
    private String workId;

    @Column(value = "from_user_id")
    private Long fromUserId;

    @Column(value = "to_user_id")
    private Long toUserId;

    @Column(value = "from_address")
    private String fromAddress;

    @Column(value = "to_address")
    private String toAddress;

    // 前一个版权持有者的区块链钱包地址，只有transferType为FULL_TRANSFER时改变否则保持原状
    @Column(value = "prev_owner_address")
    private String prevOwnerAddress;

    @Column(value = "prev_right_type")
    private RightTypeEnum prevRightType;

    // 本次 transfer 生效后的最终所有者
    @Column(value = "current_owner")
    private String currentOwner;

    @Column(value = "transfer_type")
    private TransferTypeEnum transferType;

    // 本次 transfer 生效后的最终版权类型
    @Column(value = "current_right_type")
    private String currentRightType;

    @Column(value = "license_type")
    private LicenseTypeEnum licenseType;

    @Column(value = "effective_time")
    private LocalDateTime effectiveTime;

    @Column(value = "expire_time")
    private LocalDateTime expireTime;

    @Column(value = "chain_tx_hash")
    private String chainTxHash;

    @Column(value = "block_number")
    private Long blockNumber;

    @Column(value = "block_time")
    private LocalDateTime blockTime;

    @Column(value = "contract_address")
    private String contractAddress;

    // 区块链交易状态，是否上链成功
    @Column(value = "chain_status")
    private ChainStatusEnum chainStatus;

    // 失败原因
    @Column(value = "fail_reason")
    private String failReason;

    @Column(value = "transfer_status")
    private TransferStatusEnum transferStatus;

    @Column(value = "trade_desc")
    private String tradeDesc;

    // 暂时不涉及交易价格
    @Column(value = "price")
    private BigDecimal price;

    // 扩展字段
    @Column(value = "ext")
    private String ext;
}

