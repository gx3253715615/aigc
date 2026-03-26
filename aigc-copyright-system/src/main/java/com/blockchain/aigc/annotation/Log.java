package com.blockchain.aigc.annotation;

import com.blockchain.aigc.enums.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    OperationTypeEnum operationType();

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 对象类型（如：work/order）
     */
    String targetType() default "";
}
