//SPDX-License-Identifier: UNLICENSED
pragma solidity >=0.6.10 <0.8.20;

interface IWork {
    function updateOwner(uint256 workId, address newOwner) external;

    function getWork(uint256 workId) external view returns (
        string memory,
        address,
        address,
        uint256
    );
}

contract CopyrightTransfer {

    enum LicenseType {
        NONE,
        PERSONAL,
        COMMERCIAL,
        EXCLUSIVE
    }

    // 0 1 2
    enum TransferType {
        FULL_TRANSFER,     // 所有权转让
        LICENSE_GRANT,     // 授权
        LICENSE_REVOKE     // 撤销授权（预留）
    }

    struct Transfer {
        uint256 transferId;
        uint256 workId;
        string fileHash;
        address from;
        address to;
        TransferType transferType;
        LicenseType licenseType;
        uint256 effectiveTime;
        uint256 expireTime;
        uint256 amount; // 单位 分
        uint256 timestamp;
    }

    // transferId => Transfer
    mapping(uint256 => Transfer) private transfers;

    // workId => transferIds
    mapping(uint256 => uint256[]) private workTransfers;

    address public workContract;

    event TransferCreated(
        uint256 indexed transferId,
        uint256 indexed workId,
        address indexed from,
        address to,
        string fileHash,
        TransferType transferType,
        LicenseType licenseType,
        uint256 effectiveTime,
        uint256 expireTime,
        uint256 amount,
        uint256 timestamp
    );

    constructor(address _workContract) public {
        workContract = _workContract;
    }

    /**
     * 创建转让
     */
    function createTransfer(
        uint256 transferId,
        uint256 workId,
        string memory fileHash,
        address to,
        TransferType transferType,
        LicenseType licenseType,
        uint256 effectiveTime,
        uint256 expireTime,
        uint256 amount
    ) public {

        require(transfers[transferId].timestamp == 0, "transfer exists");
        require(to != address(0), "invalid to address");

        require(amount >= 0, "invalid amount");

        // FULL_TRANSFER 不允许带 licenseType
        if (transferType == TransferType.FULL_TRANSFER) {
            require(licenseType == LicenseType.NONE, "full transfer no license");
        }
        // 授权必须带 licenseType
        if (transferType == TransferType.LICENSE_GRANT) {
            require(licenseType != LicenseType.NONE, "license required");
            require(effectiveTime > 0 && expireTime > effectiveTime, "invalid time");
        }

        (,,address owner,) = IWork(workContract).getWork(workId);

        require(msg.sender == owner, "only owner can transfer");

        transfers[transferId] = Transfer({
            transferId: transferId,
            workId: workId,
            fileHash: fileHash,
            from: owner,
            to: to,
            transferType: transferType,
            licenseType: licenseType,
            effectiveTime: effectiveTime,
            expireTime: expireTime,
            amount: amount,
            timestamp: block.timestamp
        });


        workTransfers[workId].push(transferId);

        if (transferType == TransferType.FULL_TRANSFER) {
            IWork(workContract).updateOwner(workId, to);
        }

        emit TransferCreated(
            transferId,
            workId,
            owner,
            to,
            fileHash,
            transferType,
            licenseType,
            effectiveTime,
            expireTime,
            amount,
            block.timestamp
        );
    }

    /**
     * 查询转让详情
     */
    function getTransfer(uint256 transferId)
    public
    view
    returns (
        uint256 workId,
        string memory fileHash,
        address from,
        address to,
        TransferType transferType,
        LicenseType licenseType,
        uint256 effectiveTime,
        uint256 expireTime,
        uint256 amount,
        uint256 timestamp
    )
    {
        Transfer storage t = transfers[transferId];
        require(t.timestamp != 0, "transfer not exists");

        return (
            t.workId,
            t.fileHash,
            t.from,
            t.to,
            t.transferType,
            t.licenseType,
            t.effectiveTime,
            t.expireTime,
            t.amount,
            t.timestamp
        );
    }

    /**
     * 查询作品转让历史
     */
    function getWorkTransfers(uint256 workId)
    public
    view
    returns (uint256[] memory)
    {
        return workTransfers[workId];
    }
}
