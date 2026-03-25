package com.blockchain.aigc.dto;

import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.enums.UserStatusEnum;
import lombok.Data;

@Data
public class AdminUserDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private Integer isAdmin;
    private UserAuthEnum authStatus;
    private UserStatusEnum status;
}
