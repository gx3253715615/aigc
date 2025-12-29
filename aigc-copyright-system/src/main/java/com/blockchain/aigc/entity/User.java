package com.blockchain.aigc.entity;

import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.enums.UserStatusEnum;
import com.blockchain.aigc.handler.listener.MyInsertListener;
import com.blockchain.aigc.handler.listener.MyUpdateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(value = "users", onInsert = MyInsertListener.class, onUpdate = MyUpdateListener.class)
public class User extends Base {

    @Column(value = "username")
    private String username;

    @Column(value = "password")
    private String password;

    @Column(value = "phone")
    private String phone;

    @Column(value = "email")
    private String email;

    @Column(value = "auth_status")
    private UserAuthEnum authStatus;

    @Column(value = "status")
    private UserStatusEnum status;
}

