# 智能合约部署和交互指南

## 1. 智能合约部署

### 环境准备
```bash
# 安装Node.js和npm
npm install -g truffle
npm install -g @truffle/hdwallet-provider

# 安装FISCO BCOS Web3SDK
npm install web3
```

### 合约编译
```bash
cd blockchain
npx truffle compile
```

### 合约部署脚本
```javascript
// migrations/2_deploy_contracts.js
const AIGCCopyright = artifacts.require("AIGCCopyright");

module.exports = function (deployer, network, accounts) {
    // 部署合约
    deployer.deploy(AIGCCopyright, { from: accounts[0] });
};
```

### 部署配置
```javascript
// truffle-config.js
const HDWalletProvider = require('@truffle/hdwallet-provider');

module.exports = {
    networks: {
        fisco: {
            provider: () => new HDWalletProvider(
                'your-private-key',
                'http://localhost:8545'
            ),
            network_id: "*",
            gas: 30000000,
            gasPrice: 1000000000,
        }
    },
    compilers: {
        solc: {
            version: "0.8.0",
            settings: {
                optimizer: {
                    enabled: true,
                    runs: 200
                }
            }
        }
    }
};
```

## 2. 合约交互接口

### Java服务集成

#### 添加依赖
```xml
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>4.9.8</version>
</dependency>
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>crypto</artifactId>
    <version>4.9.8</version>
</dependency>
```

#### 合约包装类生成
```bash
# 使用web3j命令行工具生成Java包装类
web3j truffle generate build/contracts/AIGCCopyright.json -o src/main/java -p com.blockchain.aigc.contract
```

#### 服务集成代码
```java
@Service
public class BlockchainContractService {
    
    @Autowired
    private Web3j web3j;
    
    @Autowired
    private Credentials credentials;
    
    @Value("${blockchain.contract-address}")
    private String contractAddress;
    
    private AIGCCopyright contract;
    
    @PostConstruct
    public void init() {
        contract = AIGCCopyright.load(
            contractAddress, 
            web3j, 
            credentials, 
            new DefaultGasProvider()
        );
    }
    
    /**
     * 调用确权合约
     */
    public String confirmCopyright(String workHash, String workId, 
                                   String creator, Long createTime, String metadata) {
        try {
            TransactionReceipt receipt = contract.confirmCopyright(
                workHash, 
                workId, 
                creator, 
                BigInteger.valueOf(createTime), 
                metadata
            ).send();
            
            // 解析事件日志
            List<AIGCCopyright.CopyrightConfirmedEventResponse> events = 
                contract.getCopyrightConfirmedEvents(receipt);
            
            if (!events.isEmpty()) {
                return events.get(0).certificateId;
            }
            
            return null;
        } catch (Exception e) {
            log.error("确权合约调用失败", e);
            throw new RuntimeException("区块链确权失败");
        }
    }
    
    /**
     * 调用转让合约
     */
    public String transferCopyright(String certificateId, String to, 
                                   Double price, String metadata) {
        try {
            TransactionReceipt receipt = contract.transferCopyright(
                certificateId, 
                to, 
                BigInteger.valueOf(price.longValue()), 
                metadata
            ).send();
            
            // 解析事件日志
            List<AIGCCopyright.CopyrightTransferredEventResponse> events = 
                contract.getCopyrightTransferredEvents(receipt);
            
            if (!events.isEmpty()) {
                return events.get(0).transferId;
            }
            
            return null;
        } catch (Exception e) {
            log.error("转让合约调用失败", e);
            throw new RuntimeException("区块链转让失败");
        }
    }
    
    /**
     * 查询确权信息
     */
    public CertificateInfo getCertificate(String certificateId) {
        try {
            Tuple7<String, String, String, String, BigInteger, BigInteger, Boolean> result = 
                contract.getCertificate(certificateId).send();
            
            return CertificateInfo.builder()
                .certificateId(result.component1())
                .workHash(result.component2())
                .workId(result.component3())
                .creator(result.component4())
                .createTime(result.component5().longValue())
                .confirmTime(result.component6().longValue())
                .isValid(result.component7())
                .build();
        } catch (Exception e) {
            log.error("查询证书失败", e);
            throw new RuntimeException("查询确权信息失败");
        }
    }
}
```

## 3. 合约测试

### 单元测试
```javascript
const AIGCCopyright = artifacts.require("AIGCCopyright");

contract("AIGCCopyright", (accounts) => {
    let contract;
    const [owner, creator, buyer] = accounts;
    
    beforeEach(async () => {
        contract = await AIGCCopyright.new({ from: owner });
    });
    
    it("应该能够确权作品", async () => {
        const workHash = "0x1234567890abcdef";
        const workId = "work123";
        const createTime = Math.floor(Date.now() / 1000);
        const metadata = JSON.stringify({ title: "Test Work", type: "image" });
        
        const result = await contract.confirmCopyright(
            workHash, workId, creator, createTime, metadata, { from: creator }
        );
        
        // 验证事件
        assert.equal(result.logs[0].event, "CopyrightConfirmed");
        assert.equal(result.logs[0].args.creator, creator);
        assert.equal(result.logs[0].args.workHash, workHash);
    });
    
    it("应该能够转让版权", async () => {
        // 先确权
        const workHash = "0x1234567890abcdef";
        const workId = "work123";
        const createTime = Math.floor(Date.now() / 1000);
        const metadata = JSON.stringify({ title: "Test Work", type: "image" });
        
        const confirmResult = await contract.confirmCopyright(
            workHash, workId, creator, createTime, metadata, { from: creator }
        );
        
        const certificateId = confirmResult.logs[0].args.certificateId;
        
        // 再转让
        const transferMetadata = JSON.stringify({ price: 100, reason: "Sale" });
        const transferResult = await contract.transferCopyright(
            certificateId, buyer, 100, transferMetadata, { from: creator }
        );
        
        // 验证事件
        assert.equal(transferResult.logs[0].event, "CopyrightTransferred");
        assert.equal(transferResult.logs[0].args.from, creator);
        assert.equal(transferResult.logs[0].args.to, buyer);
    });
    
    it("应该能够查询证书信息", async () => {
        const workHash = "0x1234567890abcdef";
        const workId = "work123";
        const createTime = Math.floor(Date.now() / 1000);
        const metadata = JSON.stringify({ title: "Test Work", type: "image" });
        
        const confirmResult = await contract.confirmCopyright(
            workHash, workId, creator, createTime, metadata, { from: creator }
        );
        
        const certificateId = confirmResult.logs[0].args.certificateId;
        const certificate = await contract.getCertificate(certificateId);
        
        assert.equal(certificate.workHash, workHash);
        assert.equal(certificate.workId, workId);
        assert.equal(certificate.creator, creator);
        assert.equal(certificate.isValid, true);
    });
});
```

## 4. 部署脚本

### 自动化部署脚本
```bash
#!/bin/bash

# deploy.sh

echo "开始部署智能合约..."

# 编译合约
truffle compile

# 部署到FISCO BCOS网络
truffle migrate --network fisco

# 获取合约地址
CONTRACT_ADDRESS=$(cat build/contracts/AIGCCopyright.json | jq -r '.networks[1337].address')

echo "合约部署成功，地址: $CONTRACT_ADDRESS"

# 更新配置文件
sed -i "s/blockchain.contract-address=.*/blockchain.contract-address=$CONTRACT_ADDRESS/" ../aigc-copyright-system/src/main/resources/application.properties

echo "部署完成！"
```

## 5. 接口文档

### RESTful API
- `POST /api/blockchain/confirm` - 作品确权
- `POST /api/blockchain/transfer` - 版权转让  
- `GET /api/blockchain/certificate/{id}` - 查询证书
- `GET /api/blockchain/transfer/{id}/history` - 查询转让历史
- `POST /api/blockchain/verify` - 验证版权

### 错误码
- `200` - 成功
- `400` - 参数错误
- `401` - 未授权
- `404` - 证书不存在
- `500` - 区块链调用失败