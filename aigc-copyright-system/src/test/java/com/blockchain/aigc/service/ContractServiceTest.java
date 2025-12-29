package com.blockchain.aigc.service;

import com.blockchain.aigc.BaseTest;
import org.fisco.bcos.sdk.BcosSDK;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ContractServiceTest extends BaseTest {
    @Autowired
    private ContractService contractService;

    @Test
    public void deploy() throws Exception {
        //String address = contractService.deployAddressAction();
        //String address = contractService.deployCopyrightCert();
        String address = contractService.deployCopyrightTransferCert();
        System.out.println("合约地址：" + address);
    }

    @Test
    public void test() throws Exception {
        BcosSDK sdk = BcosSDK.build("src/main/resources/config.toml");

    }
}
