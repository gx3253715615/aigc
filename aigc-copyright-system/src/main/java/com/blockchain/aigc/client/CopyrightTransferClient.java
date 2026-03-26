package com.blockchain.aigc.client;

import com.blockchain.aigc.contract.CopyrightCert;
import com.blockchain.aigc.contract.CopyrightTransfer;
import com.blockchain.aigc.enums.TransferTypeEnum;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author gaoxinyu
 * @date 2026/3/22 17:20
 **/
@Component
public class CopyrightTransferClient {

    static Logger logger = LoggerFactory.getLogger(CopyrightTransferClient.class);

    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;

    @Autowired
    private BaseClient baseClient;

    @PostConstruct
    public void init() throws Exception {
        initialize();

        String address = loadCopyrightTransferAddr();

        if (address == null) {
            System.out.println("首次启动，自动部署合约...");
            deployCopyrightTransferAndRecordAddr();
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

    public void deployCopyrightTransferAndRecordAddr() {

        try {
            CopyrightCert copyrightCert = CopyrightCert.deploy(client, cryptoKeyPair);
            System.out.println(
                    " deploy CopyrightTransfer success, contract address is " + copyrightCert.getContractAddress());
            recordCopyrightCertAddr(copyrightCert.getContractAddress());
        } catch (Exception e) {
            System.out.println(" deploy CopyrightTransfer contract failed, error message is  " + e.getMessage());
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
        prop.setProperty("CopyrightTransferAddress", address);
        try (FileOutputStream out = new FileOutputStream(file)) {
            prop.store(out, "contract address");
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public String loadCopyrightTransferAddr() throws Exception {
        File file = new File(BaseClient.PATH);
        if (!file.exists()) {
            return null;
        }
        Properties prop = new Properties();
        prop.load(Files.newInputStream(file.toPath()));
        String contractAddress = prop.getProperty("CopyrightTransferAddress");

        if (contractAddress == null || contractAddress.trim().isEmpty()) {
            //throw new Exception(" load CopyrightCert contract address failed, please deploy it first. ");
            return null;
        }
        logger.info(" load CopyrightTransferAddress address from contract.properties, address is {}", contractAddress);
        return contractAddress;

    }

    // 创建作品版权转让记录
    public TransactionReceipt createTransfer(String transferId, String workId,
                                             String fileHash, String to, TransferTypeEnum transferType) {
        try {
            String contractAddress = loadCopyrightTransferAddr();
            // 使用加载合约
            CopyrightTransfer copyrightTransfer = CopyrightTransfer.load(contractAddress, client, getUserCryptoKeyPair());
            // 获取链上信息
            return copyrightTransfer.createTransfer(new BigInteger(transferId), new BigInteger(workId), fileHash, to, BigInteger.valueOf(transferType.num));
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
        return null;
    }

    // 查询转让详情
    public Map<String, Object> getTransfer(String transferId) {
        try {
            String contractAddress = loadCopyrightTransferAddr();
            // 可以使用默认keyPair
            CopyrightTransfer copyrightTransfer = CopyrightTransfer.load(contractAddress, client, getUserCryptoKeyPair());
            // 获取链上信息
            Tuple6<BigInteger, String, String, String, BigInteger, BigInteger> transfer = copyrightTransfer.getTransfer(new BigInteger(transferId));
            Map<String, Object> map = new HashMap<>();
            map.put("workId", transfer.getValue1().toString());
            map.put("fileHash", transfer.getValue2());
            map.put("from", transfer.getValue3());
            map.put("to", transfer.getValue4());
            map.put("transferType", TransferTypeEnum.getTransferType(transfer.getValue5().intValue()));
            map.put("transferTime", transfer.getValue6().toString()); // TODO 时间戳转时间
            return map;
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
        return null;
    }

    // 查询作品所有转让记录
    public Map<String, Object> getWorkTransfers(String workId) {
        try {
            String contractAddress = loadCopyrightTransferAddr();
            CopyrightTransfer copyrightTransfer = CopyrightTransfer.load(contractAddress, client, getUserCryptoKeyPair());
            // 获取链上信息
            List workTransfers = copyrightTransfer.getWorkTransfers(new BigInteger(workId));
            return null;
        } catch (Exception e) {
            logger.error(" registerWork exception, error message is {}", e.getMessage());
        }
        return null;
    }

    private CryptoKeyPair getUserCryptoKeyPair() {
        String pk = baseClient.loadUserKeyPair();
        // 线程安全
        return client.getCryptoSuite().getKeyPairFactory().createKeyPair(pk);
    }
}
