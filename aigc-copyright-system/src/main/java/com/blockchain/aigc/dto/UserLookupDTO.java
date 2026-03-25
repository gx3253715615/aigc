package com.blockchain.aigc.dto;

import lombok.Data;

@Data
public class UserLookupDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
}
