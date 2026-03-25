package com.blockchain.aigc.client;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

/**
 * @author gaoxinyu
 * @date 2026/3/22 16:40
 **/
@Component
public class BaseClient {
    private BcosSDK bcosSDK;
    private Client client;
    public static final String PATH = System.getProperty("user.dir") + "/src/main/resources/contract.properties";

    @PostConstruct
    public void initialize() {
        //bcosSDK = BcosSDK.build("config.toml");
        bcosSDK = BcosSDK.build(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("config.toml")
                        .getPath()
        );

        client = bcosSDK.getClient("group0");
    }

    // 返回新创建的密钥对
    public CryptoKeyPair generateKeyPair() {
        // 生成新的密钥对
        return client.getCryptoSuite().getKeyPairFactory().generateKeyPair();
    }

    // 根据区块号获取时间戳
    public long getTimestamp(BigInteger blockNumber) {
        // 获取区块
        BcosBlock block = client.getBlockByNumber(blockNumber, true, false);
        return block.getBlock().getTimestamp();
    }
}
