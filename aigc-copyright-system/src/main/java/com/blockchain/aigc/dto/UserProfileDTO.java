package com.blockchain.aigc.dto;

import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.enums.UserStatusEnum;
import lombok.Data;

@Data
public class UserProfileDTO {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private UserAuthEnum authStatus;
    private UserStatusEnum status;
    private String walletAddress;
}
