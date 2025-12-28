// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @title AIGC版权确权合约
 * @dev 用于AIGC作品的版权确权管理
 */
contract AIGCCopyright {
    
    // 确权凭证结构
    struct Certificate {
        string certificateId;      // 证书ID
        string workHash;         // 作品哈希
        string workId;           // 作品ID
        address creator;         // 创作者地址
        uint256 createTime;      // 创作时间
        uint256 confirmTime;     // 确权时间
        bool isValid;            // 是否有效
        string metadata;         // 元数据JSON
    }
    
    // 版权转让记录结构
    struct TransferRecord {
        string transferId;       // 转让ID
        string certificateId;    // 证书ID
        address from;            // 转让方
        address to;              // 受让方
        uint256 price;           // 转让价格
        uint256 transferTime;    // 转让时间
        string metadata;         // 转让元数据
    }
    
    // 存储所有确权凭证
    mapping(string => Certificate) private certificates;
    
    // 存储用户的所有证书
    mapping(address => string[]) private userCertificates;
    
    // 存储作品的转让记录
    mapping(string => TransferRecord[]) private workTransfers;
    
    // 存储所有转让记录
    mapping(string => TransferRecord) private transfers;
    
    // 事件定义
    event CopyrightConfirmed(
        string indexed certificateId,
        string workHash,
        address indexed creator,
        uint256 confirmTime
    );
    
    event CopyrightTransferred(
        string indexed transferId,
        string indexed certificateId,
        address indexed from,
        address to,
        uint256 price,
        uint256 transferTime
    );
    
    /**
     * @dev 确认版权
     * @param workHash 作品哈希
     * @param workId 作品ID
     * @param creator 创作者地址
     * @param createTime 创作时间
     * @param metadata 元数据JSON
     * @return certificateId 确权证书ID
     */
    function confirmCopyright(
        string memory workHash,
        string memory workId,
        address creator,
        uint256 createTime,
        string memory metadata
    ) public returns (string memory certificateId) {
        require(bytes(workHash).length > 0, "作品哈希不能为空");
        require(creator != address(0), "创作者地址无效");
        require(createTime > 0, "创作时间无效");
        
        // 生成证书ID
        certificateId = generateCertificateId(workHash, creator, createTime);
        
        // 检查证书是否已存在
        require(!certificates[certificateId].isValid, "该作品已确权");
        
        // 创建确权证书
        Certificate memory certificate = Certificate({
            certificateId: certificateId,
            workHash: workHash,
            workId: workId,
            creator: creator,
            createTime: createTime,
            confirmTime: block.timestamp,
            isValid: true,
            metadata: metadata
        });
        
        // 存储证书
        certificates[certificateId] = certificate;
        userCertificates[creator].push(certificateId);
        
        // 触发事件
        emit CopyrightConfirmed(certificateId, workHash, creator, block.timestamp);
        
        return certificateId;
    }
    
    /**
     * @dev 转让版权
     * @param certificateId 证书ID
     * @param to 受让方地址
     * @param price 转让价格
     * @param metadata 转让元数据
     * @return transferId 转让记录ID
     */
    function transferCopyright(
        string memory certificateId,
        address to,
        uint256 price,
        string memory metadata
    ) public returns (string memory transferId) {
        require(bytes(certificateId).length > 0, "证书ID不能为空");
        require(to != address(0), "受让方地址无效");
        
        // 获取证书信息
        Certificate storage certificate = certificates[certificateId];
        require(certificate.isValid, "证书无效");
        require(certificate.creator == msg.sender, "只有版权所有者才能转让");
        
        // 生成转让ID
        transferId = generateTransferId(certificateId, msg.sender, to, price);
        
        // 创建转让记录
        TransferRecord memory transfer = TransferRecord({
            transferId: transferId,
            certificateId: certificateId,
            from: msg.sender,
            to: to,
            price: price,
            transferTime: block.timestamp,
            metadata: metadata
        });
        
        // 更新证书所有者
        certificate.creator = to;
        
        // 存储转让记录
        transfers[transferId] = transfer;
        workTransfers[certificateId].push(transfer);
        
        // 更新用户证书列表
        userCertificates[to].push(certificateId);
        
        // 触发事件
        emit CopyrightTransferred(transferId, certificateId, msg.sender, to, price, block.timestamp);
        
        return transferId;
    }
    
    /**
     * @dev 获取确权证书信息
     * @param certificateId 证书ID
     * @return 证书信息
     */
    function getCertificate(string memory certificateId) 
        public view returns (Certificate memory) {
        require(bytes(certificateId).length > 0, "证书ID不能为空");
        require(certificates[certificateId].isValid, "证书不存在");
        
        return certificates[certificateId];
    }
    
    /**
     * @dev 获取用户的所有证书
     * @param user 用户地址
     * @return 证书ID数组
     */
    function getUserCertificates(address user) 
        public view returns (string[] memory) {
        return userCertificates[user];
    }
    
    /**
     * @dev 获取作品的转让历史
     * @param certificateId 证书ID
     * @return 转让记录数组
     */
    function getWorkTransferHistory(string memory certificateId) 
        public view returns (TransferRecord[] memory) {
        return workTransfers[certificateId];
    }
    
    /**
     * @dev 获取转让记录
     * @param transferId 转让ID
     * @return 转让记录
     */
    function getTransferRecord(string memory transferId) 
        public view returns (TransferRecord memory) {
        return transfers[transferId];
    }
    
    /**
     * @dev 验证作品版权
     * @param workHash 作品哈希
     * @param creator 声称的创作者
     * @return 是否拥有版权
     */
    function verifyCopyright(string memory workHash, address creator) 
        public view returns (bool) {
        string[] memory certificates = userCertificates[creator];
        
        for (uint i = 0; i < certificates.length; i++) {
            Certificate memory cert = certificates[certificates[i]];
            if (keccak256(bytes(cert.workHash)) == keccak256(bytes(workHash)) && cert.isValid) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @dev 生成证书ID
     */
    function generateCertificateId(
        string memory workHash,
        address creator,
        uint256 createTime
    ) private pure returns (string memory) {
        return string(abi.encodePacked(
            "CERT",
            uint2str(uint256(keccak256(abi.encodePacked(workHash, creator, createTime))) % 100000000)
        ));
    }
    
    /**
     * @dev 生成转让ID
     */
    function generateTransferId(
        string memory certificateId,
        address from,
        address to,
        uint256 price
    ) private pure returns (string memory) {
        return string(abi.encodePacked(
            "TRANS",
            uint2str(uint256(keccak256(abi.encodePacked(certificateId, from, to, price))) % 100000000)
        ));
    }
    
    /**
     * @dev 将uint转换为字符串
     */
    function uint2str(uint256 _i) private pure returns (string memory) {
        if (_i == 0) {
            return "0";
        }
        uint256 j = _i;
        uint256 length;
        while (j != 0) {
            length++;
            j /= 10;
        }
        bytes memory bstr = new bytes(length);
        uint256 k = length;
        j = _i;
        while (j != 0) {
            bstr[--k] = bytes1(uint8(48 + j % 10));
            j /= 10;
        }
        return string(bstr);
    }
}