pragma solidity >=0.6.10 <0.8.20;

/**
 * @title CopyrightCert
 * @dev 作品确权合约（最小证据模型）
 */
contract CopyrightCert {

    struct Work {
        string fileHash;        // 文件哈希（核心证据）
        address authorAddress;  // 作者地址
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

    /**
     * @dev 作者确权（手动触发上链）
     * @param workId 作品ID
     */
    function confirmCopyright(uint256 workId, string calldata fileHash) public {

        require(!works[workId].exists, "work already exists");
        require(bytes(fileHash).length > 0, "invalid hash");

        uint256 time = block.timestamp;

        works[workId] = Work({
            fileHash: fileHash,
            authorAddress: msg.sender,
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
     * @dev 查询作品确权信息
     */
    function getWork(uint256 workId)
    public
    view
    returns (
        string memory fileHash,
        address authorAddress,
        uint256 certifyTime
    )
    {
        Work memory w = works[workId];
        require(w.exists, "work not exists");

        return (
            w.fileHash,
            w.authorAddress,
            w.certifyTime
        );
    }
}
