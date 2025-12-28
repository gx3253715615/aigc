package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.FileTypeEnum;
import com.blockchain.aigc.enums.LicenseTypeEnum;
import com.blockchain.aigc.enums.WorkStatusEnum;
import com.blockchain.aigc.enums.RightTypeEnum;
import com.blockchain.aigc.handler.listener.MyInsertListener;
import com.blockchain.aigc.handler.listener.MyUpdateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@Table(value = "works", onInsert = MyInsertListener.class, onUpdate = MyUpdateListener.class)
public class Work extends Base {

    // 唯一标识一个作品，可用于搜索
    @Column(value = "work_id")
    private String workId;

    @Column(value = "user_id")
    private Long userId;

    @Column(value = "file_name")
    private String fileName;

    @Column(value = "file_type")
    private FileTypeEnum fileType;

    @Column(value = "file_size")
    private Long fileSize;

    @Column(value = "minio_path")
    private String minioPath;

    @Column(value = "file_hash")
    private String fileHash;

    // 作品简介
    @Column(value = "summary")
    private String summary;

    @Column(value = "model_name")
    private String modelName;

    @Column(value = "model_version")
    private String modelVersion;

    // 模型来源, 可以不填，可以是比如deepseek来源是百炼平台、第三方接口平台，可以自定义填写
    @Column(value = "model_source")
    private String modelSource;

    // 数据库存储json格式，包括温度、top_k等数据
    @Column(value = "model_params", typeHandler = Fastjson2TypeHandler.class)
    private Map<String, Object> modelParams;

    @Column(value = "prompt")
    private String prompt;

    // 可为空，创建作品方式，人工创作、人机合作等，暂时先用不到
    @Column(value = "creation_type")
    private String creationType;

    // 作品状态
    @Column(value = "work_status")
    private WorkStatusEnum workStatus;

    // 版权类型
    @Column(value = "right_type")
    private RightTypeEnum rightType;

    // 授权类型
    @Column(value = "license_type")
    private LicenseTypeEnum licenseType;

    // 链上交易hash
    @Column(value = "chain_tx_hash")
    private String chainTxHash;

    // 区块高度
    @Column(value = "block_number")
    private Long blockNumber;
}

