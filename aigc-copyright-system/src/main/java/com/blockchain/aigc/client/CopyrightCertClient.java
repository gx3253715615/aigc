package com.blockchain.aigc.client;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.blockchain.aigc.contract.CopyrightCert;
import org.checkerframework.checker.units.qual.C;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author gaoxinyu
 * @date 2026/3/21 20:15
 **/
@Component
public class CopyrightCertClient {

    static Logger logger = LoggerFactory.getLogger(CopyrightCertClient.class);

    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;

    @PostConstruct
    public void init() throws Exception {
        initialize();

        String address = loadCopyrightCertAddr();

        if (address == null) {
            System.out.println("首次启动，自动部署合约...");
            deployCopyrightCertAndRecordAddr();
        } else {
            System.out.println("检测到已有合约地址：" + address);
        }
    }

    public void initialize() {
        bcosSDK = BcosSDK.build(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("config.toml")
                        .getPath()
        );
        client = bcosSDK.getClient();
        cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
        logger.debug("create client for group, account address is {}", cryptoKeyPair.getAddress());
    }

    public void deployCopyrightCertAndRecordAddr() {
        try {
            CopyrightCert copyrightCert = CopyrightCert.deploy(client, cryptoKeyPair);
            System.out.println(
                    " deploy copyrightcert success, contract address is " + copyrightCert.getContractAddress());
            recordCopyrightCertAddr(copyrightCert.getContractAddress());
        } catch (Exception e) {
            System.out.println(" deploy copyright contract failed, error message is  " + e.getMessage());
        }
    }


    public void recordCopyrightCertAddr(String address) throws IOException {
        Properties prop = new Properties();
        File file = new File(BaseClient.PATH);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                prop.load(in);
            }
        }
        prop.setProperty("CopyrightCertAddress", address);
        try (FileOutputStream out = new FileOutputStream(file)) {
            prop.store(out, "contract address");
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public String loadCopyrightCertAddr() throws Exception {
        File file = new File(BaseClient.PATH);
        if (!file.exists()) {
            return null;
        }
        Properties prop = new Properties();
        prop.load(Files.newInputStream(file.toPath()));
        String contractAddress = prop.getProperty("CopyrightCertAddress");

        if (contractAddress == null || contractAddress.trim().isEmpty()) {
            //throw new Exception(" load CopyrightCert contract address failed, please deploy it first. ");
            return null;
        }
        logger.info(" load CopyrightCert address from contract.properties, address is {}", contractAddress);
        return contractAddress;
    }

    // 链上注册作品
    public void registerWork(String workId, String fileHash, String author) {
        try {
            String contractAddress = loadCopyrightCertAddr();
            CopyrightCert copyrightCert = CopyrightCert.load(contractAddress, client, cryptoKeyPair);
            // 获取链上信息
            TransactionReceipt receipt = copyrightCert.registerWork(new BigInteger(workId), fileHash, author);
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
    }

    // 链上确权作品
    public void confirmCopyright(String workId) {
        try {
            String contractAddress = loadCopyrightCertAddr();
            CopyrightCert copyrightCert = CopyrightCert.load(contractAddress, client, cryptoKeyPair);
            // 获取链上信息
            TransactionReceipt receipt = copyrightCert.confirmCopyright(new BigInteger(workId));
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
    }

    // 查询链上作品信息
    public Map<String, Object> getWork(String workId) {
        try {
            String contractAddress = loadCopyrightCertAddr();
            CopyrightCert copyrightCert = CopyrightCert.load(contractAddress, client, cryptoKeyPair);
            // 获取链上信息
            Tuple3<String, String, BigInteger> res = copyrightCert.getWork(new BigInteger(workId));
            Map<String, Object> map = new HashMap<>();
            map.put("fileHash", res.getValue1());
            map.put("author", res.getValue2());
            map.put("certifyTime", res.getValue3().toString());
            return map;
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
        return null;
    }
}
