package com.blockchain.aigc.service;

import cn.hutool.core.util.IdUtil;
import com.blockchain.aigc.dto.CopyrightTransferRequest;
import com.blockchain.aigc.dto.TransferHistoryDTO;
import com.blockchain.aigc.entity.CopyrightTransfer;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.UserWallet;
import com.blockchain.aigc.entity.Work;
import com.blockchain.aigc.enums.*;
import com.blockchain.aigc.mapper.CopyrightTransferMapper;
import com.blockchain.aigc.mapper.UserWalletMapper;
import com.blockchain.aigc.utils.UserUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.blockchain.aigc.entity.table.CopyrightTransferTableDef.COPYRIGHT_TRANSFER;
import static com.blockchain.aigc.entity.table.UserWalletTableDef.USER_WALLET;

@Service
public class CopyrightService extends ServiceImpl<CopyrightTransferMapper, CopyrightTransfer> {

    @Autowired
    private CopyrightTransferMapper copyrightTransferMapper;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private WorkService workService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlockchainService blockchainService;

    @Transactional(rollbackFor = Exception.class)
    public TransferHistoryDTO transferCopyright(CopyrightTransferRequest request) {
        User fromUser = UserUtil.get();
        if (fromUser == null) {
            throw new RuntimeException("用户未登录");
        }
            
        // 检查用户认证状态
        if (fromUser.getAuthStatus() != UserAuthEnum.AUTH) {
            throw new RuntimeException("用户未通过实名认证，无法进行版权转让");
        }
            
        User toUser = userService.getUserById(request.getToUserId());
        if (toUser == null) {
            throw new RuntimeException("受让方用户不存在");
        }

        Work work = workService.getWorkEntityByWorkId(request.getWorkId());
        if (work == null) {
            throw new RuntimeException("作品不存在");
        }

        if (!work.getUserId().equals(fromUser.getId())) {
            throw new RuntimeException("无权转让该作品");
        }

        if (work.getWorkStatus() != WorkStatusEnum.CERTIFIED) {
            throw new RuntimeException("作品未确权，无法转让");
        }

        // 获取双方钱包地址
        QueryWrapper fromWalletQuery = QueryWrapper.create()
                .where(USER_WALLET.USER_ID.eq(fromUser.getId()));
        UserWallet fromWallet = userWalletMapper.selectOneByQuery(fromWalletQuery);

        QueryWrapper toWalletQuery = QueryWrapper.create()
                .where(USER_WALLET.USER_ID.eq(toUser.getId()));
        UserWallet toWallet = userWalletMapper.selectOneByQuery(toWalletQuery);

        if (fromWallet == null || toWallet == null) {
            throw new RuntimeException("钱包地址不存在");
        }

        // 创建转让记录
        CopyrightTransfer transfer = new CopyrightTransfer();
        transfer.setTransferId(IdUtil.getSnowflakeNextIdStr());
        transfer.setWorkId(work.getWorkId());
        transfer.setFromUserId(fromUser.getId());
        transfer.setToUserId(toUser.getId());
        transfer.setFromAddress(fromWallet.getWalletAddress());
        transfer.setToAddress(toWallet.getWalletAddress());
        transfer.setPrevOwnerAddress(fromWallet.getWalletAddress());
        transfer.setPrevRightType(work.getRightType());
        transfer.setTransferType(request.getTransferType());
        transfer.setLicenseType(request.getLicenseType());
        transfer.setEffectiveTime(request.getEffectiveTime() != null ? request.getEffectiveTime() : LocalDateTime.now());
        transfer.setExpireTime(request.getExpireTime());
        transfer.setTradeDesc(request.getTradeDesc());
        transfer.setPrice(request.getPrice());
        transfer.setChainStatus(ChainStatusEnum.PENDING);
        transfer.setTransferStatus(TransferStatusEnum.INIT);

        // 确定转让后的版权类型和所有者
        if (request.getTransferType() == TransferTypeEnum.FULL_TRANSFER) {
            transfer.setCurrentOwner(toWallet.getWalletAddress());
            transfer.setCurrentRightType(RightTypeEnum.RIGHT_OWNERSHIP.name());
        } else {
            transfer.setCurrentOwner(fromWallet.getWalletAddress());
            transfer.setCurrentRightType(RightTypeEnum.RIGHT_USAGE.name());
        }

        copyrightTransferMapper.insert(transfer);

        // 调用区块链服务进行链上转让
        try {
            String txHash = blockchainService.transferCopyright(
                    work.getWorkId(),
                    work.getFileHash(),
                    fromWallet.getWalletAddress(),
                    toWallet.getWalletAddress(),
                    request.getTransferType()
            );

            transfer.setChainTxHash(txHash);
            transfer.setChainStatus(ChainStatusEnum.SUCCESS);
            transfer.setTransferStatus(TransferStatusEnum.CONFIRMED);
            copyrightTransferMapper.update(transfer);

            // 更新作品状态
            if (request.getTransferType() == TransferTypeEnum.FULL_TRANSFER) {
                workService.updateWorkStatus(work.getWorkId(), WorkStatusEnum.TRANSFERRED, txHash, null);
            }
        } catch (Exception e) {
            transfer.setChainStatus(ChainStatusEnum.FAILED);
            transfer.setFailReason(e.getMessage());
            copyrightTransferMapper.update(transfer);
            throw new RuntimeException("链上转让失败: " + e.getMessage(), e);
        }

        // 转换为DTO返回
        TransferHistoryDTO dto = new TransferHistoryDTO();
        BeanUtils.copyProperties(transfer, dto);
        dto.setFromUserName(fromUser.getUsername());
        dto.setToUserName(toUser.getUsername());

        return dto;
    }

    public Page<TransferHistoryDTO> getTransferHistory(String workId, int pageNum, int pageSize) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(COPYRIGHT_TRANSFER.WORK_ID.eq(workId))
                .orderBy(COPYRIGHT_TRANSFER.CREATE_TIME.desc());

        Page<CopyrightTransfer> page = copyrightTransferMapper.paginate(pageNum, pageSize, queryWrapper);

        List<TransferHistoryDTO> dtoList = page.getRecords().stream().map(transfer -> {
            TransferHistoryDTO dto = new TransferHistoryDTO();
            BeanUtils.copyProperties(transfer, dto);

            User fromUser = userService.getUserById(transfer.getFromUserId());
            User toUser = userService.getUserById(transfer.getToUserId());

            if (fromUser != null) {
                dto.setFromUserName(fromUser.getUsername());
            }
            if (toUser != null) {
                dto.setToUserName(toUser.getUsername());
            }

            return dto;
        }).collect(Collectors.toList());

        Page<TransferHistoryDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setPageNumber(page.getPageNumber());
        dtoPage.setPageSize(page.getPageSize());
        dtoPage.setTotalRow(page.getTotalRow());

        return dtoPage;
    }

    public TransferHistoryDTO getTransferById(Long id) {
        CopyrightTransfer transfer = copyrightTransferMapper.selectOneById(id);
        if (transfer == null) {
            throw new RuntimeException("转让记录不存在");
        }

        TransferHistoryDTO dto = new TransferHistoryDTO();
        BeanUtils.copyProperties(transfer, dto);

        User fromUser = userService.getUserById(transfer.getFromUserId());
        User toUser = userService.getUserById(transfer.getToUserId());

        if (fromUser != null) {
            dto.setFromUserName(fromUser.getUsername());
        }
        if (toUser != null) {
            dto.setToUserName(toUser.getUsername());
        }

        return dto;
    }
}
