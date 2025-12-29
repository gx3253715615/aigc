package com.blockchain.aigc.service;

import com.blockchain.aigc.AddressAction;
import com.blockchain.aigc.contract.CopyrightCert;
import com.blockchain.aigc.contract.CopyrightTransfer;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 跑单测用
 * @author gaoxinyu
 * @date 2025/12/29 4:20
 **/
@Service
public class ContractService {

    @Autowired
    private Client client;

    public String deployAddressAction() throws Exception {
        CryptoKeyPair adminKeyPair = client.getCryptoSuite().createKeyPair();
        AddressAction contract = AddressAction.deploy(client, adminKeyPair);
        return contract.getContractAddress();
    }

    public String deployCopyrightCert() throws Exception {
        CryptoKeyPair adminKeyPair = client.getCryptoSuite().createKeyPair();
        CopyrightCert contract = CopyrightCert.deploy(client, adminKeyPair);
        return contract.getContractAddress();
    }

    public String deployCopyrightTransferCert() throws Exception {
        CryptoKeyPair adminKeyPair = client.getCryptoSuite().createKeyPair();
        CopyrightTransfer contract = CopyrightTransfer.deploy(client, adminKeyPair);
        return contract.getContractAddress();
    }
}


