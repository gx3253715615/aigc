package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.OperationTypeEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@Table("operation_log")
public class OperationLog {

    @Id(keyType = KeyType.Auto)
    private Long id;

    // 操作用户ID
    @Column(value = "user_id")
    private Long userId;

    // 用户名
    @Column(value = "username")
    private String username;

    // 操作类型
    @Column(value = "operation_type")
    private OperationTypeEnum operationType;

    // 模块
    @Column(value = "module")
    private String module;

    // 操作对象ID
    @Column(value = "target_id")
    private Long targetId;

    // 操作对象类型
    @Column(value = "target_type")
    private String targetType;

    // 操作描述
    @Column(value = "description")
    private String description;

    // 请求方式
    @Column(value = "request_method")
    private String requestMethod;

    // 请求路径
    @Column(value = "request_url")
    private String requestUrl;

    // 请求IP
    @Column(value = "ip")
    private String ip;

    // 浏览器信息
    @Column(value = "user_agent")
    private String userAgent;

    // 操作时间
    @Column(value = "create_time")
    private LocalDateTime createTime;
}
