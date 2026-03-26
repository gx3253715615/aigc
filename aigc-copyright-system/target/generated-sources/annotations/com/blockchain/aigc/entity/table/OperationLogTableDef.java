package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class OperationLogTableDef extends TableDef {

    /**
     * 操作日志实体类
     */
    public static final OperationLogTableDef OPERATION_LOG = new OperationLogTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn IP = new QueryColumn(this, "ip");

    public final QueryColumn MODULE = new QueryColumn(this, "module");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn TARGET_ID = new QueryColumn(this, "target_id");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn USER_AGENT = new QueryColumn(this, "user_agent");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn REQUEST_URL = new QueryColumn(this, "request_url");

    public final QueryColumn TARGET_TYPE = new QueryColumn(this, "target_type");

    public final QueryColumn DESCRIPTION = new QueryColumn(this, "description");

    public final QueryColumn OPERATION_TYPE = new QueryColumn(this, "operation_type");

    public final QueryColumn REQUEST_METHOD = new QueryColumn(this, "request_method");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, IP, MODULE, USER_ID, TARGET_ID, USERNAME, USER_AGENT, CREATE_TIME, REQUEST_URL, TARGET_TYPE, DESCRIPTION, OPERATION_TYPE, REQUEST_METHOD};

    public OperationLogTableDef() {
        super("", "operation_log");
    }

    private OperationLogTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public OperationLogTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new OperationLogTableDef("", "operation_log", alias));
    }

}
