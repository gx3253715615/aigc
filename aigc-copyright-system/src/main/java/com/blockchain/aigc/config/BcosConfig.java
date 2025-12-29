package com.blockchain.aigc.config;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BcosConfig {

    @Bean
    public Client client() {
        //BcosSDK sdk = BcosSDK.build("aigc-copyright-system/src/main/resources/config.toml");
        BcosSDK sdk = BcosSDK.build("src/main/resources/config.toml");
        //BcosSDK sdk = BcosSDK.build("classpath:config.toml");
        return sdk.getClient(1); // groupId
    }
}
