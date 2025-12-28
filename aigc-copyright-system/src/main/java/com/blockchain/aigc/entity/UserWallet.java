package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.ChainTypeEnum;
import com.blockchain.aigc.handler.listener.MyInsertListener;
import com.blockchain.aigc.handler.listener.MyUpdateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;

@Data
@Accessors(chain = true)
@Table(value = "user_wallets", onInsert = MyInsertListener.class, onUpdate = MyUpdateListener.class)
public class UserWallet extends Base {

    @Column(value = "user_id")
    private Long userId;

    // 目前只有 fisco-bcos
    @Column(value = "chain_type")
    private ChainTypeEnum chainType;

    @Column(value = "wallet_address")
    private String walletAddress;
}

