package com.blockchain.aigc.enums;

import lombok.Getter;

/**
 * 操作类型枚举
 */
@Getter
public enum OperationTypeEnum {
    CREATE("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    REGISTER("注册"),
    LOGIN("登录"),
    AUTH("实名认证"),
    LOGOUT("登出"),
    DOWNLOAD("下载");

    private final String description;

    OperationTypeEnum(String description) {
        this.description = description;
    }
}
