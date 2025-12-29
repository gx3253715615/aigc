pragma solidity ^0.4.25;

/**
 * @title CopyrightTransfer
 * @dev 版权转让与授权合约（行为驱动模型）
 */
contract CopyrightTransfer {

    enum RightType {
        RIGHT_OWNERSHIP,
        RIGHT_USAGE
    }

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
        uint256 timestamp;
    }

    // transferId => Transfer
    mapping(uint256 => Transfer) private transfers;

    // workId => transferIds
    mapping(uint256 => uint256[]) private workTransfers;

    /**
     * @dev 创建转让 / 授权记录
     */
    function createTransfer(
        uint256 transferId,
        uint256 workId,
        string fileHash,
        address to,
        TransferType transferType
    ) public {

        require(transfers[transferId].timestamp == 0, "transfer exists");

        address from = getCurrentOwner(workId);

        // 首次转让，from 允许为 0x0（初始确权）
        if (from != address(0)) {
            require(msg.sender == from, "only owner can transfer");
        }

        transfers[transferId] = Transfer({
            transferId: transferId,
            workId: workId,
            fileHash: fileHash,
            from: from,
            to: to,
            transferType: transferType,
            timestamp: now
        });

        workTransfers[workId].push(transferId);
    }

    /**
     * @dev 查询转让详情
     */
    function getTransfer(uint256 transferId)
    public
    view
    returns (
        uint256 workId,
        string fileHash,
        address from,
        address to,
        TransferType transferType,
        uint256 timestamp
    )
    {
        Transfer memory t = transfers[transferId];
        require(t.timestamp != 0, "transfer not exists");

        return (
            t.workId,
            t.fileHash,
            t.from,
            t.to,
            t.transferType,
            t.timestamp
        );
    }

    /**
     * @dev 查询作品转让历史
     */
    function getWorkTransfers(uint256 workId)
    public
    view
    returns (uint256[])
    {
        return workTransfers[workId];
    }

    /**
     * @dev 查询当前所有者（推导）
     */
    function getCurrentOwner(uint256 workId)
    public
    view
    returns (address)
    {
        uint256[] memory ids = workTransfers[workId];
        if (ids.length == 0) {
            return address(0);
        }

        for (uint256 i = ids.length; i > 0; i--) {
            Transfer memory t = transfers[ids[i - 1]];
            if (t.transferType == TransferType.FULL_TRANSFER) {
                return t.to;
            }
        }

        return address(0);
    }

    /**
     * @dev 查询当前权利类型（推导）
     */
    function getCurrentRightType(uint256 workId)
    public
    view
    returns (RightType)
    {
        uint256[] memory ids = workTransfers[workId];
        if (ids.length == 0) {
            return RightType.RIGHT_OWNERSHIP;
        }

        Transfer memory t = transfers[ids[ids.length - 1]];

        if (t.transferType == TransferType.LICENSE_GRANT) {
            return RightType.RIGHT_USAGE;
        }

        return RightType.RIGHT_OWNERSHIP;
    }
}
