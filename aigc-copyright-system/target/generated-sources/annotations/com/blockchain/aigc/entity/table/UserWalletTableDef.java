package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserWalletTableDef extends TableDef {

    public static final UserWalletTableDef USER_WALLET = new UserWalletTableDef();

    /**
     * 主键
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 逻辑删除
     */
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    public final QueryColumn CHAIN_TYPE = new QueryColumn(this, "chain_type");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn PRIVATE_KEY = new QueryColumn(this, "private_key");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 创建人id
     */
    public final QueryColumn CREATE_USER_ID = new QueryColumn(this, "create_user_id");

    /**
     * 更新人id
     */
    public final QueryColumn UPDATE_USER_ID = new QueryColumn(this, "update_user_id");

    public final QueryColumn WALLET_ADDRESS = new QueryColumn(this, "wallet_address");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, USER_ID, CHAIN_TYPE, CREATE_TIME, PRIVATE_KEY, UPDATE_TIME, CREATE_USER_ID, UPDATE_USER_ID, WALLET_ADDRESS};

    public UserWalletTableDef() {
        super("", "user_wallets");
    }

    private UserWalletTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserWalletTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserWalletTableDef("", "user_wallets", alias));
    }

}
