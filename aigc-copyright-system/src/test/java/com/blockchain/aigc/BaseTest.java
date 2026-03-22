package com.blockchain.aigc;

import com.blockchain.aigc.client.BaseClient;
import com.blockchain.aigc.client.CopyrightCertClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author gaoxinyu
 * @date 2025/12/28 22:36
 **/
@SpringBootTest
public class BaseTest {

    // 测试连接
    @Autowired
    private BaseClient baseClient;

    @Autowired
    private CopyrightCertClient copyrightCertClient;

    @Test
    public void testClient() {
        copyrightCertClient.registerWork("123456", "654321", "0x123456");
        System.out.println("注册成功");
        copyrightCertClient.confirmCopyright("123456");
        System.out.println("确权成功");
        Map<String, Object> work = copyrightCertClient.getWork("123456");
        System.out.println("111");
    }
}
