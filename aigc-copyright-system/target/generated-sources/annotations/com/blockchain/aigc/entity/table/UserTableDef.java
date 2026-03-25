package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserTableDef extends TableDef {

    public static final UserTableDef USER = new UserTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    public final QueryColumn IS_ADMIN = new QueryColumn(this, "is_admin");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn AUTH_STATUS = new QueryColumn(this, "auth_status");

    public final QueryColumn AVATAR_PATH = new QueryColumn(this, "avatar_path");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn CREATE_USER_ID = new QueryColumn(this, "create_user_id");

    public final QueryColumn UPDATE_USER_ID = new QueryColumn(this, "update_user_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EMAIL, PHONE, STATUS, IS_ADMIN, PASSWORD, USERNAME, AUTH_STATUS, AVATAR_PATH, CREATE_TIME, UPDATE_TIME, CREATE_USER_ID, UPDATE_USER_ID};

    public UserTableDef() {
        super("", "users");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("", "users", alias));
    }

}
