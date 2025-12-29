package com.blockchain.aigc;

import org.fisco.bcos.sdk.client.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gaoxinyu
 * @date 2025/12/28 22:36
 **/
@SpringBootTest
public class BaseTest {

    // 测试连接
    @Autowired
    private Client client;

    //@Test
    public void testClient() {
        // 获取最新高度
        System.out.println(client.getBlockNumber().getBlockNumber());
    }
}
