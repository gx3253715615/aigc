package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserRealnameTableDef extends TableDef {

    public static final UserRealnameTableDef USER_REALNAME = new UserRealnameTableDef();

    /**
     * 主键
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 逻辑删除
     */
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    public final QueryColumn ID_NUMBER = new QueryColumn(this, "id_number");

    public final QueryColumn REAL_NAME = new QueryColumn(this, "real_name");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn FAIL_REASON = new QueryColumn(this, "fail_reason");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn VERIFY_TIME = new QueryColumn(this, "verify_time");

    /**
     * 创建人id
     */
    public final QueryColumn CREATE_USER_ID = new QueryColumn(this, "create_user_id");

    /**
     * 更新人id
     */
    public final QueryColumn UPDATE_USER_ID = new QueryColumn(this, "update_user_id");

    public final QueryColumn VERIFY_STATUS = new QueryColumn(this, "verify_status");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, ID_NUMBER, REAL_NAME, CREATE_TIME, FAIL_REASON, UPDATE_TIME, VERIFY_TIME, CREATE_USER_ID, UPDATE_USER_ID, VERIFY_STATUS};

    public UserRealnameTableDef() {
        super("", "user_realname");
    }

    private UserRealnameTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserRealnameTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserRealnameTableDef("", "user_realname", alias));
    }

}
