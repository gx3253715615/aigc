package com.blockchain.aigc.enums;

/**
 * @author gaoxinyu
 * @date 2025/12/28 10:50
 **/
public enum TransferTypeEnum {
    FULL_TRANSFER(0),
    LICENSE_GRANT(1);
    public int num;

    // 构造e
    TransferTypeEnum(int num) {
    }

    // get
    public int getNum() {
        return num;
    }
}
