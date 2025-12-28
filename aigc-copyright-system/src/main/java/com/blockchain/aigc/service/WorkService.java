package com.blockchain.aigc.service;

import cn.hutool.core.util.IdUtil;
import com.blockchain.aigc.dto.UploadWorkRequest;
import com.blockchain.aigc.dto.WorkDTO;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.Work;
import com.blockchain.aigc.enums.FileTypeEnum;
import com.blockchain.aigc.enums.LicenseTypeEnum;
import com.blockchain.aigc.enums.RightTypeEnum;
import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.enums.WorkStatusEnum;
import com.blockchain.aigc.mapper.WorkMapper;
import com.blockchain.aigc.utils.FileHashUtil;
import com.blockchain.aigc.utils.UserUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.blockchain.aigc.entity.table.WorkTableDef.WORK;

@Service
public class WorkService extends ServiceImpl<WorkMapper, Work> {

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private UserService userService;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Transactional(rollbackFor = Exception.class)
    public WorkDTO uploadWork(MultipartFile file, UploadWorkRequest request) {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        // 检查用户认证状态
        if (currentUser.getAuthStatus() != UserAuthEnum.AUTH) {
            throw new RuntimeException("用户未通过实名认证，无法上传作品");
        }

        try {
            // 生成文件hash
            String fileHash = FileHashUtil.calculateFileHash(file);

            // 检查文件是否已存在
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(WORK.FILE_HASH.eq(fileHash));
            Work existWork = workMapper.selectOneByQuery(queryWrapper);
            if (existWork != null) {
                throw new RuntimeException("该文件已存在");
            }

            // 生成workId
            String workId = IdUtil.getSnowflakeNextIdStr();

            // 上传文件到MinIO到根目录
            String objectName = workId + "_" + file.getOriginalFilename();
            minioService.uploadFile(file, objectName);

            // 判断文件类型
            FileTypeEnum fileType = determineFileType(file.getOriginalFilename());

            // 保存作品信息
            Work work = new Work();
            work.setWorkId(workId);
            work.setUserId(currentUser.getId());
            work.setFileName(file.getOriginalFilename());
            work.setFileType(fileType);
            work.setFileSize(file.getSize());
            work.setMinioPath(bucketName + "/" + objectName);
            work.setFileHash(fileHash);
            work.setSummary(request.getSummary());
            work.setModelName(request.getModelName());
            work.setModelVersion(request.getModelVersion());
            work.setModelSource(request.getModelSource());

            // 直接存储模型参数Map
            if (request.getModelParams() != null && !request.getModelParams().isEmpty()) {
                // 将Map<String, String>转换为Map<String, Object>以匹配数据库字段类型
                work.setModelParams(new HashMap<>(request.getModelParams()));
            }

            work.setPrompt(request.getPrompt());
            work.setCreationType(request.getCreationType());
            work.setWorkStatus(WorkStatusEnum.UPLOADED);
            work.setRightType(RightTypeEnum.RIGHT_OWNERSHIP);
            work.setLicenseType(LicenseTypeEnum.PERSONAL);

            workMapper.insert(work);

            // 转换为DTO
            WorkDTO dto = new WorkDTO();
            BeanUtils.copyProperties(work, dto);
            dto.setUserName(currentUser.getUsername());
            dto.setFileUrl(minioService.getFileUrl(objectName));

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("上传作品失败: " + e.getMessage(), e);
        }
    }

    public WorkDTO getWorkById(Long id) {
        Work work = workMapper.selectOneById(id);
        if (work == null) {
            throw new RuntimeException("作品不存在");
        }

        WorkDTO dto = new WorkDTO();
        BeanUtils.copyProperties(work, dto);

        User user = userService.getUserById(work.getUserId());
        if (user != null) {
            dto.setUserName(user.getUsername());
        }

        dto.setFileUrl(minioService.getFileUrl(work.getMinioPath()));

        return dto;
    }

    public WorkDTO getWorkByWorkId(String workId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(WORK.WORK_ID.eq(workId));
        Work work = workMapper.selectOneByQuery(queryWrapper);

        if (work == null) {
            throw new RuntimeException("作品不存在");
        }

        WorkDTO dto = new WorkDTO();
        BeanUtils.copyProperties(work, dto);

        User user = userService.getUserById(work.getUserId());
        if (user != null) {
            dto.setUserName(user.getUsername());
        }

        dto.setFileUrl(minioService.getFileUrl(work.getMinioPath()));

        return dto;
    }

    public Page<WorkDTO> getMyWorks(int pageNum, int pageSize) {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(WORK.USER_ID.eq(currentUser.getId()))
                .orderBy(WORK.CREATE_TIME.desc());

        Page<Work> page = workMapper.paginate(pageNum, pageSize, queryWrapper);

        List<WorkDTO> dtoList = page.getRecords().stream().map(work -> {
            WorkDTO dto = new WorkDTO();
            BeanUtils.copyProperties(work, dto);

            User user = userService.getUserById(work.getUserId());
            if (user != null) {
                dto.setUserName(user.getUsername());
            }

            dto.setFileUrl(minioService.getFileUrl(work.getMinioPath()));

            return dto;
        }).collect(Collectors.toList());

        Page<WorkDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setPageNumber(page.getPageNumber());
        dtoPage.setPageSize(page.getPageSize());
        dtoPage.setTotalRow(page.getTotalRow());

        return dtoPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWorkStatus(String workId, WorkStatusEnum status, String chainTxHash, Long blockNumber) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(WORK.WORK_ID.eq(workId));
        Work work = workMapper.selectOneByQuery(queryWrapper);

        if (work == null) {
            throw new RuntimeException("作品不存在");
        }

        work.setWorkStatus(status);
        if (chainTxHash != null) {
            work.setChainTxHash(chainTxHash);
        }
        if (blockNumber != null) {
            work.setBlockNumber(blockNumber);
        }

        workMapper.update(work);
    }

    private FileTypeEnum determineFileType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "txt":
            case "doc":
            case "docx":
            case "pdf":
                return FileTypeEnum.TEXT;
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return FileTypeEnum.IMAGE;
            case "mp3":
            case "wav":
            case "flac":
            case "aac":
                return FileTypeEnum.AUDIO;
            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
                return FileTypeEnum.VIDEO;
            default:
                return FileTypeEnum.OTHER;
        }
    }

    public Work getWorkEntityByWorkId(String workId) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(WORK.WORK_ID.eq(workId));
        return workMapper.selectOneByQuery(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void certifyWork(String workId) {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
            
        // 检查用户认证状态
        if (currentUser.getAuthStatus() != UserAuthEnum.AUTH) {
            throw new RuntimeException("用户未通过实名认证，无法确权作品");
        }
            
        Work work = getWorkEntityByWorkId(workId);
        if (work == null) {
            throw new RuntimeException("作品不存在");
        }
            
        // 修改确权逻辑
        //if (!work.getUserId().equals(currentUser.getId())) {
        //    throw new RuntimeException("无权确权该作品");
        //}
            
        if (work.getWorkStatus() == WorkStatusEnum.CERTIFIED) {
            throw new RuntimeException("作品已确权");
        }
            
        // TODO: Call blockchain service to certify
        updateWorkStatus(workId, WorkStatusEnum.CERTIFIED, null, null);
    }
}
