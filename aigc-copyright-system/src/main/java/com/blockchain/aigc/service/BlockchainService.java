package com.blockchain.aigc.service;

import com.blockchain.aigc.AddressAction;
import com.blockchain.aigc.contract.CopyrightCert;
import com.blockchain.aigc.contract.CopyrightTransfer;
import com.blockchain.aigc.enums.TransferTypeEnum;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 区块链服务 - 与FISCO BCOS交互
 */
@Service
public class BlockchainService {

    @Autowired
    private Client client;

    private final static String addressActionAddress =  "0xfa6ac41d0e064cfb50321de940cd7a7907b2dede";
    private final static String confirmCopyrightAddress =  "0xc6360c8743da3a720b8e92490ca795de989a55ab";
    private final static String copyrightTransferAddress =  "0xa5d55e5f14bee905809cf1e4d189a17770bc589e";

    /**
     * 获取一个新的钱包地址
     */
    public CryptoKeyPair createCryptoKeyPair() {
         return client.getCryptoSuite().createKeyPair();
    }

    /**
     * 恢复CryptoKeyPair
     */
    public CryptoKeyPair getCryptoKeyPairByPrivateKey(String privateKey) {
        return client.getCryptoSuite().createKeyPair(privateKey);
    }

    public void doAction(String privateKey) throws Exception {
        AddressAction contract = AddressAction.load(addressActionAddress, client, getCryptoKeyPairByPrivateKey(privateKey));
        contract.doAction("auth success");
    }

    /**
     * 确权作品
     */
    public Map<String, String> confirmCopyright(String privateKey, String workId, String fileHash) {
        CopyrightCert contract = CopyrightCert.load(confirmCopyrightAddress, client, getCryptoKeyPairByPrivateKey(privateKey));
        contract.registerWork(BigInteger.valueOf(Long.parseLong(workId)), fileHash, getCryptoKeyPairByPrivateKey(privateKey).getAddress());
        TransactionReceipt receipt = contract.confirmCopyright(BigInteger.valueOf(Long.parseLong(workId)));
        Map<String, String> map = new HashMap<>();
        map.put("txHash", receipt.getTransactionHash());
        map.put("blockNumber", receipt.getBlockNumber());
        return map;
    }

    /**
     * 查询作品链上基本信息
     */
    public Map<String, Object> selectConfirmCopyright(String privateKey, String workId) throws ContractException {
        CopyrightCert contract = CopyrightCert.load(confirmCopyrightAddress, client, getCryptoKeyPairByPrivateKey(privateKey));
        Tuple3<String, String, BigInteger> work = contract.getWork(BigInteger.valueOf(Long.parseLong(workId)));
        Map<String, Object> map = new HashMap<>();
        map.put("fileHash", work.getValue1());
        map.put("author", work.getValue2());
        map.put("time", work.getValue3());
        return map;
    }


    /**
     * 转让版权
     */
    public String transferCopyright(String transId, String workId, String fileHash, String fromAddress, String toAddress, TransferTypeEnum transferType) {
        CopyrightTransfer contract = CopyrightTransfer.load(copyrightTransferAddress, client, getCryptoKeyPairByPrivateKey(fromAddress));
        TransactionReceipt transfer = contract.createTransfer(
                BigInteger.valueOf(Long.parseLong(transId)),
                BigInteger.valueOf(Long.parseLong(workId)),
                fileHash,
                toAddress,
                BigInteger.valueOf(transferType.num));
        return transfer.getTransactionHash();
    }

    /**
     * 查询版权证书
     */
    public Map<String, Object> getCertificate(String workId) {
        // TODO: 实现与FISCO BCOS智能合约的交互
        // 调用智能合约的getCertificate方法

        Map<String, Object> result = new HashMap<>();
        result.put("workId", workId);
        result.put("exists", false);

        return result;
    }

    /**
     * 验证版权
     */
    public boolean verifyCopyright(String workId, String fileHash) {
        // TODO: 实现与FISCO BCOS智能合约的交互
        // 调用智能合约的verifyCopyright方法

        return false;
    }

    /**
     * 查询转让历史（从链上）
     */
    public Object[] getWorkTransferHistory(String workId) {
        // TODO: 实现与FISCO BCOS智能合约的交互
        // 调用智能合约的getWorkTransferHistory方法

        return new Object[0];
    }
}
