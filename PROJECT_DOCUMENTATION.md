# 基于联盟链的AIGC作品版权确权与转让管理系统

## 项目概述

这是一个基于联盟链技术的AIGC（AI Generated Content）作品版权确权与转让管理系统。系统利用FISCO BCOS联盟链和智能合约技术，为AI生成作品提供可信的版权保护、确权存证和转让管理功能。

## 核心功能

### 1. 联盟链部署
- 基于 FISCO BCOS 构建联盟链网络
- 智能合约用于版权确权与转让状态变更
- 链上仅存储作品哈希、版权人地址和时间戳等凭证级数据

### 2. 用户管理
- 用户注册、登录与身份认证
- 区块链钱包地址绑定
- JWT基于token的身份验证

### 3. 作品管理
- 支持文本、图像、音频、视频等多种AIGC作品上传
- 自动生成作品数字指纹（SHA-256哈希）
- MinIO分布式文件存储
- 作品元数据管理（创作时间、AI模型、prompt等）

### 4. 版权确权
- 版权确权通过 调用智能合约完成，系统支持 手动触发确权，即作品上传完成后由用户主动申请确权

### 5. 版权转让
- 支持：所有权转让（FULL_TRANSFER） 使用权授权（LICENSE_GRANT）
- 智能合约负责：更新链上当前版权状态 触发转让事件

### 6. 链上查询
- 链上仅支持：单作品确权信息查询 当前版权状态查询
- 转让历史查询：通过监听链上事件 同步至数据库后查询

### 7. 可视化界面
- 现代化的Web前端界面
- 作品上传和管理界面
- 版权确权和转让操作
- 区块链查询和验证功能

## 技术架构

```
┌─────────────────────────────────────────────────────────────┐
│                    前端层 (Vue 3 + TS)                      │
├─────────────────────────────────────────────────────────────┤
│  用户界面 │ 作品管理 │ 确权管理 │ 转让管理 │ 链上查询 │ 系统管理  │
└─────────────────────┬─────────────────────────────────────────┘
                    │ HTTP/REST API
┌───────────────────┴─────────────────────────────────────────┐
│                  应用服务层 (Spring Boot)                   │
├─────────────────────────────────────────────────────────────┤
│  用户服务 │ 作品服务 │ 版权服务 │ 存储服务 │ 区块链服务       │
│  JWT认证 │ 文件上传 │ 确权管理 │ 转让管理 │ FISCO BCOS交互  │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────┴─────────────────────────────────────────┐
│                    数据存储层                               │
├─────────────────────────────────────────────────────────────┤
│  MySQL: 用户/作品/交易数据  │  MinIO: AIGC作品文件存储     │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────┴─────────────────────────────────────────┐
│                   区块链层 (FISCO BCOS)                    │
├─────────────────────────────────────────────────────────────┤
│  智能合约：版权确权合约 │ 版权转让合约 │ 查询合约               │
│  存证数据：作品哈希 │ 确权凭证 │ 转让记录                     │
└─────────────────────────────────────────────────────────────┘
```

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 8.0 + MyBatis Plus 3.5.3.1
- **文件存储**: MinIO 8.5.7
- **区块链**: Web3j 4.9.8 (FISCO BCOS交互)
- **安全**: Spring Security + JWT 0.11.5
- **工具**: FastJSON 2.0.42, Apache Commons

### 前端技术栈
- **框架**: Vue 3.4 + TypeScript
- **UI组件**: Element Plus 2.6.1
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4
- **HTTP**: Axios 1.6.8
- **区块链**: Ethers.js 6.11.1
- **工具**: Crypto-JS, Day.js

### 区块链技术
- **平台**: FISCO BCOS联盟链
- **合约语言**: Solidity 0.8.0
- **共识机制**: PBFT
- **智能合约**: 版权确权、版权转让、查询验证

## 智能合约设计

### 版权确权合约 (AIGCCopyright.sol)

```solidity
// 主要功能
- confirmCopyright() - 作品确权
- transferCopyright() - 版权转让
- getCertificate() - 查询证书
- getWorkTransferHistory() - 查询转让历史
- verifyCopyright() - 版权验证
```

## API接口设计

### 用户相关接口
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/profile` - 获取用户信息
- `PUT /api/users/profile` - 更新用户信息

### 作品相关接口
- `POST /api/works/upload` 上传文件
- `POST /api/works/{id}/certify` 手动确权
- `GET /api/works/{id}` 获取作品信息

### 版权转让接口
- `POST /api/copyright/transfer` 转让接口
- `GET /api/copyright/transfers?workId=` 查询转让历史 

## 数据库设计

### 用户表 (users)
- id
- username
- password
- phone(nullable)
- email(nullable)
- auth_status(INIT、AUTH)
- status(ENABLE、DISABLE)
- 审计信息（createBy、updateBy、delete、updateTime、createTime）

### 实名认证表（user_realname）
- id
- userId
- realName
- idNumber
- verifyTime
- verifyStatus(PENDING、APPROVED、REJECTED)
- failReason
- 审计信息（createBy、updateBy、delete、updateTime、createTime）

### 用户钱包表（user_wallets）
- id
- userId
- chainType（FISCO_BCOS、ETH、OTHER）
- walletAddress
- create_time

### 作品表 (works)
链上
- workId
- fileHash
- authorAddress
- certifyTime
- workType

链下
- id （主键UUID / 雪花ID）
- 作者信息（id）
- 作品基本信息（文件名、文件类型、文件大小）
- MinIO存储路径（文件存储地址）
- 文件哈希值（存储在智能合约中的凭证）
- 作品摘要（用于展示）
- 模型信息（模型名称、来源、版本、参数、提示词）
- creationType （扩展字段）
- 作品状态（INIT / UPLOADED / CERTIFIED / OFFLINE / TRANSFERRED）
- 交易hash
- 区块高度（blockNumber）
- 版权类型（RIGHT_OWNERSHIP / RIGHT_USAGE）（所有权、使用权）
- 授权信息（licenseType（个人 / 商用 / 独占））
- 审计操作（creatBy、updateBy、delete、updateTime、createTime）

### 版权转让表 (copyright_transfers)
链上
- workId
- fileHash
- fromAddress、toAddress
- currentOwner
- currentRightType（RIGHT_OWNERSHIP、RIGHT_USAGE）-> 本次 transferType 生效后的权利类型
- transferType（FULL_TRANSFER（转让）、LICENSE_GRANT（授权））
- timestamp

链下
- id
- transferId（UUID、雪花ID（递增））
- workId
- 转让双方信息（fromUserId、toUserId、fromAddress、toAddress）
- prevOwnerAddress（前一个版权持有者的区块链钱包地址，只有transferType为FULL_TRANSFER时改变否则保持原状）
- prevRightType（变更前链上记录的版权类型 - RIGHT_OWNERSHIP、RIGHT_USAGE）
- currentOwner（本次 transfer 生效后的最终所有者）
- transferType（FULL_TRANSFER（转让）、LICENSE_GRANT（授权）、LICENSE_REVOKE（撤销授权（预留）））
- currentRightType（RIGHT_OWNERSHIP（所有权）、RIGHT_USAGE（使用权）
- licenseType（PERSONAL（个人使用）、COMMERCIAL（商用）、EXCLUSIVE（独占））
- 有效期与业务约束 （effectiveTime、expireTime（永久可为空））
- 数据库中的链上信息：chainTxHash、blockNumber、blockTime、contractAddress、chainStatus（PENDING、SUCCESS、FAILED）
- failReason（可为空）
- transferStatus（INIT、CONFIRMED、CANCELLED、EXPIRED）
  注释：chainStatus = SUCCESS AND transferStatus = CONFIRMED => 版权变更正式生效
- tradeDesc（交易说明（用于展示 / 审计））
- price（版权交易费用--扩展用）
- bizId（幂等字段（UUID））
- ext（扩展字段）
- 审计操作（creatBy、updateBy、delete、updateTime、createTime）

### 1. 身份认证
- JWT基于token的身份验证
- 密码加密存储
- 登录状态管理

### 2. 文件安全
- 文件哈希校验防篡改
- 文件类型和大小限制
- MinIO访问权限控制

### 3. 区块链安全
- 私钥安全管理
- 交易签名验证
- 智能合约权限控制
- 操作审计日志

### 4. 应用安全
- CORS跨域配置
- SQL注入防护
- XSS攻击防护
- 输入参数验证


TODO
角色
认证完之后刷新页面

## 部署说明

### 环境要求
- Java 8+
- Node.js 16+
- MySQL 8.0
- Redis 6.0+
- MinIO
- FISCO BCOS节点

### 后端部署

### 前端部署

### 区块链部署

## 使用流程
### 1. 用户注册登录
1. 用户在前端界面注册账号
2. 用户进行实名认证
3. 自动生成并绑定区块链钱包地址
4. 登录系统获得JWT token

### 2. 作品上传
1. 选择AIGC作品文件
2. 填写作品信息（标题、描述、AI模型等）
3. 系统自动生成文件hash
4. 文件存储到MinIO
5. 作品信息保存到数据库

### 3. 版权确权
1. 用户申请作品确权（手动触发）
2. 系统调用智能合约
3. 作品哈希值上链存证

### 4. 版权转让
1. 选择要转让的作品
2. 指定受让方
3. 系统调用转让合约
4. 链上记录转让信息
5. 更新版权归属

### 5. 链上查询
1. 查询某个作品的版权信息
2. 查询转让历史
3. 查询某个用户的版权信息

## 项目特色
### 1. 技术创新

### 2. 业务创新


### 3. 应用价值

## 后续优化方向

### 1. 功能扩展


### 2. 性能优化


### 3. 安全增强

### 4. 生态建设

## 联系信息

本项目是一个完整的区块链应用系统，适合作为毕业设计项目。系统结合了最新的区块链技术和实际业务需求，具有很好的技术深度和应用价值。

如有问题或需要进一步完善，欢迎提交Issue或联系项目维护者。
