package com.blockchain.aigc.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class CopyrightTransferTableDef extends TableDef {

    public static final CopyrightTransferTableDef COPYRIGHT_TRANSFER = new CopyrightTransferTableDef();

    /**
     * 主键
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn EXT = new QueryColumn(this, "ext");

    public final QueryColumn PRICE = new QueryColumn(this, "price");

    public final QueryColumn WORK_ID = new QueryColumn(this, "work_id");

    /**
     * 逻辑删除
     */
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    public final QueryColumn TO_USER_ID = new QueryColumn(this, "to_user_id");

    public final QueryColumn BLOCK_TIME = new QueryColumn(this, "block_time");

    public final QueryColumn TO_ADDRESS = new QueryColumn(this, "to_address");

    public final QueryColumn TRADE_DESC = new QueryColumn(this, "trade_desc");

    /**
     * 创建时间
     */
    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn EXPIRE_TIME = new QueryColumn(this, "expire_time");

    public final QueryColumn FAIL_REASON = new QueryColumn(this, "fail_reason");

    public final QueryColumn FROM_USER_ID = new QueryColumn(this, "from_user_id");

    public final QueryColumn TRANSFER_ID = new QueryColumn(this, "transfer_id");

    /**
     * 更新时间
     */
    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn BLOCK_NUMBER = new QueryColumn(this, "block_number");

    public final QueryColumn CHAIN_STATUS = new QueryColumn(this, "chain_status");

    public final QueryColumn CHAIN_TX_HASH = new QueryColumn(this, "chain_tx_hash");

    public final QueryColumn FROM_ADDRESS = new QueryColumn(this, "from_address");

    public final QueryColumn LICENSE_TYPE = new QueryColumn(this, "license_type");

    /**
     * 创建人id
     */
    public final QueryColumn CREATE_USER_ID = new QueryColumn(this, "create_user_id");

    public final QueryColumn CURRENT_OWNER = new QueryColumn(this, "current_owner");

    public final QueryColumn TRANSFER_TYPE = new QueryColumn(this, "transfer_type");

    /**
     * 更新人id
     */
    public final QueryColumn UPDATE_USER_ID = new QueryColumn(this, "update_user_id");

    public final QueryColumn EFFECTIVE_TIME = new QueryColumn(this, "effective_time");

    public final QueryColumn PREV_RIGHT_TYPE = new QueryColumn(this, "prev_right_type");

    public final QueryColumn TRANSFER_STATUS = new QueryColumn(this, "transfer_status");

    public final QueryColumn CONTRACT_ADDRESS = new QueryColumn(this, "contract_address");

    public final QueryColumn CURRENT_RIGHT_TYPE = new QueryColumn(this, "current_right_type");

    public final QueryColumn PREV_OWNER_ADDRESS = new QueryColumn(this, "prev_owner_address");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, EXT, PRICE, WORK_ID, TO_USER_ID, BLOCK_TIME, TO_ADDRESS, TRADE_DESC, CREATE_TIME, EXPIRE_TIME, FAIL_REASON, FROM_USER_ID, TRANSFER_ID, UPDATE_TIME, BLOCK_NUMBER, CHAIN_STATUS, CHAIN_TX_HASH, FROM_ADDRESS, LICENSE_TYPE, CREATE_USER_ID, CURRENT_OWNER, TRANSFER_TYPE, UPDATE_USER_ID, EFFECTIVE_TIME, PREV_RIGHT_TYPE, TRANSFER_STATUS, CONTRACT_ADDRESS, CURRENT_RIGHT_TYPE, PREV_OWNER_ADDRESS};

    public CopyrightTransferTableDef() {
        super("", "copyright_transfers");
    }

    private CopyrightTransferTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public CopyrightTransferTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new CopyrightTransferTableDef("", "copyright_transfers", alias));
    }

}
