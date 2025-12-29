pragma solidity ^0.4.25;

/**
 * @title CopyrightCert
 * @dev 作品确权合约（最小证据模型）
 */
contract CopyrightCert {

    struct Work {
        string fileHash;        // 文件哈希（核心证据）
        address authorAddress;  // 作者地址
        uint256 certifyTime;    // 确权时间（0 表示未确权）
        bool exists;            // 是否已登记
    }

    // 雪花ID => 作品
    mapping(uint256 => Work) private works;

    /**
     * @dev 登记作品（确权前）
     * @param workId 雪花算法生成的作品ID
     * @param fileHash 文件哈希
     * @param author 作者地址
     */
    function registerWork(
        uint256 workId,
        string fileHash,
        address author
    ) public {

        require(!works[workId].exists, "work already exists");

        works[workId] = Work({
            fileHash: fileHash,
            authorAddress: author,
            certifyTime: 0,
            exists: true
        });
    }

    /**
     * @dev 作者确权（手动触发上链）
     * @param workId 作品ID
     */
    function confirmCopyright(uint256 workId) public {

        Work storage w = works[workId];

        require(w.exists, "work not exists");
        require(msg.sender == w.authorAddress, "only author can confirm");
        require(w.certifyTime == 0, "already confirmed");

        // 使用区块时间作为确权时间
        w.certifyTime = now;
    }

    /**
     * @dev 查询作品确权信息
     */
    function getWork(uint256 workId)
    public
    view
    returns (
        string fileHash,
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
