package com.blockchain.aigc;

import com.blockchain.aigc.client.BaseClient;
import com.blockchain.aigc.client.CopyrightCertClient;
import com.blockchain.aigc.service.MinioService;
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

    @Autowired
    private MinioService minioService;

    //@Test
    public void testClient() {
        copyrightCertClient.registerWork("123456", "654321", "0x123456");
        System.out.println("注册成功");
        copyrightCertClient.confirmCopyright("123456");
        System.out.println("确权成功");
        Map<String, Object> work = copyrightCertClient.getWork("123456");
        System.out.println("111");
    }

    // 测试MinIO
    //@Test
    public void testMinIO() {
        String objectName = "test.txt";
        String url = minioService.uploadFile(null, objectName);
        System.out.println(url);
        minioService.deleteFile(objectName);
    }
}
