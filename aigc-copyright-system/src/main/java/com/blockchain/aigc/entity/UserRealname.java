package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.VerifyStatusEnum;
import com.blockchain.aigc.handler.listener.MyInsertListener;
import com.blockchain.aigc.handler.listener.MyUpdateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Table(value = "user_realname", onInsert = MyInsertListener.class, onUpdate = MyUpdateListener.class)
public class UserRealname extends Base {

    @Column(value = "user_id")
    private Long userId;

    @Column(value = "real_name")
    private String realName;

    @Column(value = "id_number")
    private String idNumber;

    @Column(value = "verify_time")
    private LocalDateTime verifyTime;

    @Column(value = "verify_status")
    private VerifyStatusEnum verifyStatus;

    @Column(value = "fail_reason")
    private String failReason;
}

