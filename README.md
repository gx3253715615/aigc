# AIGC版权保护系统

基于联盟链的AIGC作品版权确权与转让管理系统

## 项目结构

```
aigc-copyright-system/
├── aigc-copyright-system/          # Spring Boot后端
│   ├── src/main/java/com/blockchain/aigc/
│   │   ├── entity/                 # 实体类
│   │   ├── mapper/                 # MyBatis Mapper
│   │   ├── service/                # 业务逻辑层
│   │   ├── controller/             # 控制层
│   │   ├── config/                 # 配置类
│   │   └── utils/                  # 工具类
│   └── src/main/resources/
│       ├── application.properties   # 配置文件
│       └── mapper/                # MyBatis XML映射文件
└── aigc-copyright-frontend/       # Vue 3前端
    ├── src/
    │   ├── api/                    # API接口
    │   ├── components/             # 公共组件
    │   ├── views/                  # 页面组件
    │   ├── stores/                 # Pinia状态管理
    │   ├── router/                 # 路由配置
    │   ├── utils/                  # 工具函数
    │   └── types/                  # TypeScript类型定义
    └── public/                     # 静态资源

```

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0
- MinIO (对象存储)
- Web3j (区块链交互)
- JWT (身份认证)

### 前端
- Vue 3.4
- TypeScript
- Element Plus (UI组件库)
- Pinia (状态管理)
- Vue Router 4
- Axios (HTTP请求)
- Ethers.js (区块链交互)

### 区块链
- FISCO BCOS联盟链
- Solidity智能合约

## 功能模块

1. **用户管理**
   - 用户注册/登录
   - 身份认证
   - 钱包地址绑定

2. **作品管理**
   - 作品上传
   - 数字指纹生成
   - 元数据管理

3. **版权确权**
   - 智能合约确权
   - 区块链存证
   - 确权凭证生成

4. **版权转让**
   - 转让申请
   - 智能合约记录
   - 转让历史查询

5. **链上查询**
   - 确权记录查询
   - 转让记录查询
   - 区块信息查看

## 快速开始

### 环境要求
- Java 8+
- Node.js 16+
- MySQL 8.0
- Redis
- MinIO
- FISCO BCOS节点

### 后端启动
```bash
cd aigc-copyright-system
mvn clean install
mvn spring-boot:run
```

### 前端启动
```bash
cd aigc-copyright-frontend
npm install
npm run dev
```

### 数据库初始化
```sql
CREATE DATABASE IF NOT EXISTS aigc_copyright;
USE aigc_copyright;

-- 执行数据库表结构SQL脚本
```

## 智能合约接口设计

### 版权确权合约
```solidity
function confirmCopyright(
    string memory workHash,
    string memory workId,
    address creator,
    uint256 createTime
) public returns (string memory certificateId);
```

### 版权转让合约
```solidity
function transferCopyright(
    string memory certificateId,
    address from,
    address to,
    uint256 price,
    uint256 transferTime
) public returns (string memory transferId);
```

### 查询合约
```solidity
function getCertificate(string memory certificateId) 
    public view returns (Certificate memory);

function getTransferHistory(string memory certificateId)
    public view returns (Transfer[] memory);
```

## API接口文档

### 用户相关
- POST /api/users/register - 用户注册
- POST /api/users/login - 用户登录
- GET /api/users/profile - 获取用户信息
- PUT /api/users/profile - 更新用户信息

### 作品相关
- POST /api/works/upload - 上传作品
- GET /api/works/list - 获取作品列表
- GET /api/works/{id} - 获取作品详情
- POST /api/works/{id}/confirm - 作品确权

### 版权转让
- POST /api/transfers/create - 创建转让
- GET /api/transfers/list - 获取转让记录
- POST /api/transfers/{id}/confirm - 确认转让

### 区块链查询
- GET /api/blockchain/certificate/{certificateId} - 查询确权凭证
- GET /api/blockchain/transfer/{txHash} - 查询转让记录

## 安全考虑

1. **文件安全**
   - 文件哈希校验
   - MinIO访问控制
   - 文件类型限制

2. **区块链安全**
   - 私钥安全管理
   - 交易签名验证
   - 智能合约权限控制

3. **应用安全**
   - JWT身份认证
   - 密码加密存储
   - SQL注入防护
   - XSS攻击防护

## 部署说明

### 生产环境配置
1. 配置SSL证书
2. 配置反向代理(Nginx)
3. 配置数据库连接池
4. 配置Redis集群
5. 配置MinIO集群
6. 配置FISCO BCOS生产节点

### Docker部署
```dockerfile
# 后端Dockerfile
FROM openjdk:8-jre-slim
COPY target/aigc-copyright-system.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# 前端Dockerfile
FROM node:16-alpine as build
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
```

## 联系信息

如有问题，请提交Issue或联系项目维护者。