package com.blockchain.aigc.enums;

/**
 * @author gaoxinyu
 * @date 2025/12/28 10:32
 **/
public enum WorkStatusEnum {
    INIT,
    UPLOADING,
    UPLOADED,
    CERTIFYING, // 确权中
    CERTIFIED, // 已确权
    TRANSFERRED, // 已发生转让 / 授权, 但是作品仍然存在
    OFFLINE // 已下架，或者删除，或者版权过期等
}
