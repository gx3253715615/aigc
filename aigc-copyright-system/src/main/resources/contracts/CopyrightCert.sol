// SPDX-License-Identifier: UNLICENSED
pragma solidity >=0.6.10 <0.8.20;

contract CopyrightCert {

    struct Work {
        string fileHash;        // 文件哈希（核心证据）
        address authorAddress;  // 作者地址
        address owner;          // 持有者
        uint256 certifyTime;    // 确权时间（0 表示未确权）
        bool exists;            // 是否已确权
    }

    mapping(uint256 => Work) private works;

    event CopyrightConfirmed(
        uint256 indexed workId,
        string fileHash,
        address indexed author,
        uint256 certifyTime
    );

    event OwnerUpdated(
        uint256 indexed workId,
        address indexed oldOwner,
        address indexed newOwner
    );

    /**
     * 确权
     */
    function confirmCopyright(uint256 workId, string calldata fileHash) public {

        require(!works[workId].exists, "work already exists");
        require(bytes(fileHash).length > 0, "invalid hash");

        uint256 time = block.timestamp;

        works[workId] = Work({
            fileHash: fileHash,
            authorAddress: msg.sender,
            owner: msg.sender,
            certifyTime: time,
            exists: true
        });

        emit CopyrightConfirmed(
            workId,
            fileHash,
            msg.sender,
            time
        );
    }

    /**
     * 修改 owner
     */
    function updateOwner(uint256 workId, address newOwner) external {

        Work storage w = works[workId];
        require(w.exists, "not exists");

        address oldOwner = w.owner;
        w.owner = newOwner;

        emit OwnerUpdated(workId, oldOwner, newOwner);
    }

    /**
     * 查询作品信息
     */
    function getWork(uint256 workId)
    public
    view
    returns (
        string memory fileHash,
        address authorAddress,
        address owner,
        uint256 certifyTime
    )
    {
        Work memory w = works[workId];
        require(w.exists, "work not exists");

        return (
            w.fileHash,
            w.authorAddress,
            w.owner,
            w.certifyTime
        );
    }
}
