package com.blockchain.aigc.service;

import com.blockchain.aigc.BaseTest;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    //@Test
    void register() {
        User user = new User();
        user.setUsername("test1");
        user.setPassword("123456");
        //boolean save = userService.save(user);
        int insert = userMapper.insert(user);
        assertEquals(1, insert);
    }
}
