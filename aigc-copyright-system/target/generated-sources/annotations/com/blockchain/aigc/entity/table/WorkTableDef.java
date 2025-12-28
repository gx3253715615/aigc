package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class WorkTableDef extends TableDef {

    public static final WorkTableDef WORK = new WorkTableDef();

    /**
     * 主键
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn PROMPT = new QueryColumn(this, "prompt");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn WORK_ID = new QueryColumn(this, "work_id");

    /**
     * 逻辑删除
     */
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    public final QueryColumn SUMMARY = new QueryColumn(this, "summary");

    public final QueryColumn FILE_HASH = new QueryColumn(this, "file_hash");

    public final QueryColumn FILE_NAME = new QueryColumn(this, "file_name");

    public final QueryColumn FILE_SIZE = new QueryColumn(this, "file_size");

    public final QueryColumn FILE_TYPE = new QueryColumn(this, "file_type");

    public final QueryColumn MINIO_PATH = new QueryColumn(this, "minio_path");

    public final QueryColumn MODEL_NAME = new QueryColumn(this, "model_name");

    public final QueryColumn RIGHT_TYPE = new QueryColumn(this, "right_type");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn WORK_STATUS = new QueryColumn(this, "work_status");

    public final QueryColumn BLOCK_NUMBER = new QueryColumn(this, "block_number");

    public final QueryColumn CHAIN_TX_HASH = new QueryColumn(this, "chain_tx_hash");

    public final QueryColumn LICENSE_TYPE = new QueryColumn(this, "license_type");

    public final QueryColumn MODEL_PARAMS = new QueryColumn(this, "model_params");

    public final QueryColumn MODEL_SOURCE = new QueryColumn(this, "model_source");

    /**
     * 创建人id
     */
    public final QueryColumn CREATE_USER_ID = new QueryColumn(this, "create_user_id");

    public final QueryColumn CREATION_TYPE = new QueryColumn(this, "creation_type");

    public final QueryColumn MODEL_VERSION = new QueryColumn(this, "model_version");

    /**
     * 更新人id
     */
    public final QueryColumn UPDATE_USER_ID = new QueryColumn(this, "update_user_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, PROMPT, USER_ID, WORK_ID, SUMMARY, FILE_HASH, FILE_NAME, FILE_SIZE, FILE_TYPE, MINIO_PATH, MODEL_NAME, RIGHT_TYPE, CREATE_TIME, UPDATE_TIME, WORK_STATUS, BLOCK_NUMBER, CHAIN_TX_HASH, LICENSE_TYPE, MODEL_PARAMS, MODEL_SOURCE, CREATE_USER_ID, CREATION_TYPE, MODEL_VERSION, UPDATE_USER_ID};

    public WorkTableDef() {
        super("", "works");
    }

    private WorkTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public WorkTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new WorkTableDef("", "works", alias));
    }

}
