package com.blockchain.aigc.service;

import com.blockchain.aigc.BaseTest;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.UserWallet;
import com.blockchain.aigc.mapper.UserMapper;
import com.blockchain.aigc.mapper.UserWalletMapper;
import com.mybatisflex.core.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.blockchain.aigc.entity.table.UserWalletTableDef.USER_WALLET;


class WorkServiceTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWalletMapper userWalletMapper;

    //@Test
    public void testGetUserWalletByUserId() {
        // 测试根据用户ID查询用户钱包信息
        Long userId = 17L; // 示例用户ID

        // 首先查询用户信息
        User user = userMapper.selectOneById(userId);
        if (user != null) {
            System.out.println("用户信息: " + user.getUsername());

            // 根据用户ID查询钱包信息
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(USER_WALLET.USER_ID.eq(userId));
            UserWallet wallet = userWalletMapper.selectOneByQuery(queryWrapper);

            if (wallet != null) {
                System.out.println("钱包地址: " + wallet.getWalletAddress());
                System.out.println("链类型: " + wallet.getChainType());
                System.out.println("创建时间: " + wallet.getCreateTime());
            } else {
                System.out.println("用户未找到钱包信息");
            }
        } else {
            System.out.println("用户不存在");
        }
    }
}
