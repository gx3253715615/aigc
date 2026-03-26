package com.blockchain.aigc.controller;

import com.blockchain.aigc.annotation.Log;
import com.blockchain.aigc.dto.ApiResponse;
import com.blockchain.aigc.dto.UploadWorkRequest;
import com.blockchain.aigc.dto.WorkDTO;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.Work;
import com.blockchain.aigc.enums.AuditStatusEnum;
import com.blockchain.aigc.enums.OperationTypeEnum;
import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.service.UserService;
import com.blockchain.aigc.service.WorkService;
import com.blockchain.aigc.service.MinioService;
import com.blockchain.aigc.utils.LogContextUtil;
import com.blockchain.aigc.utils.UserUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/api/works")
@CrossOrigin
public class WorkController {

    @Autowired
    private WorkService workService;

    @Autowired
    private UserService userService;

    @Autowired
    private MinioService minioService;

    /**
     * 上传作品
     */
    @PostMapping("/upload")
    @Log(module = "作品", operationType = OperationTypeEnum.CREATE, description = "上传作品", targetType = "work")
    public ApiResponse<WorkDTO> uploadWork(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "summary", required = false) String summary,
            @RequestParam(value = "modelName", required = false) String modelName,
            @RequestParam(value = "modelVersion", required = false) String modelVersion,
            @RequestParam(value = "modelSource", required = false) String modelSource,
            @RequestParam(value = "modelParams", required = false) String modelParamsJson,
            @RequestParam(value = "prompt", required = false) String prompt,
            @RequestParam(value = "creationType", required = false) String creationType) {
        try {
            // 检查用户认证状态
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return ApiResponse.error("用户未登录");
            }

            if (currentUser.getAuthStatus() != UserAuthEnum.AUTH) {
                return ApiResponse.error("用户未通过实名认证，无法上传作品");
            }

            UploadWorkRequest request = new UploadWorkRequest();
            request.setFileName(file.getOriginalFilename());
            request.setSummary(summary);
            request.setModelName(modelName);
            request.setModelVersion(modelVersion);
            request.setModelSource(modelSource);
            request.setPrompt(prompt);
            request.setCreationType(creationType);

            // 解析模型参数JSON为Map<String, String>
            if (modelParamsJson != null && !modelParamsJson.isEmpty()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> modelParams = objectMapper.readValue(
                            modelParamsJson,
                            new TypeReference<Map<String, String>>() {}
                    );
                    request.setModelParams(modelParams);
                } catch (Exception e) {
                    return ApiResponse.error("模型参数格式错误: " + e.getMessage());
                }
            }

            WorkDTO result = workService.uploadWork(file, request);
            return ApiResponse.success("上传成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取作品详情
     */
    @GetMapping("/{id}")
    public ApiResponse<WorkDTO> getWork(@PathVariable Long id) {
        try {
            WorkDTO work = workService.getWorkById(id);
            return ApiResponse.success(work);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户作品列表
     */
    @GetMapping("/my")
    public ApiResponse<Page<WorkDTO>> getMyWorks(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<WorkDTO> page = workService.getMyWorks(pageNum, pageSize);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/admin/list")
    public ApiResponse<Page<WorkDTO>> getAdminWorkList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) AuditStatusEnum status) {
        try {
            Page<WorkDTO> page = workService.getAdminWorkList(pageNum, pageSize, keyword, status);
            return ApiResponse.success(page);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/admin/{id}/audit")
    @Log(module = "作品", operationType = OperationTypeEnum.UPDATE, description = "审核作品", targetType = "work")
    public ApiResponse<String> auditWork(
            @PathVariable Long id,
            @RequestParam AuditStatusEnum status) {
        try {
            workService.auditWork(id, status);
            return ApiResponse.success("审核成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 手动确权
     */
    @PostMapping("/{id}/certify")
    @Log(module = "作品", operationType = OperationTypeEnum.UPDATE, description = "作品确权", targetType = "work")
    public ApiResponse<String> certifyWork(@PathVariable Long id) {
        try {
            // 检查用户认证状态
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                return ApiResponse.error("用户未登录");
            }

            if (currentUser.getAuthStatus() != UserAuthEnum.AUTH) {
                return ApiResponse.error("用户未通过实名认证，无法确权作品");
            }

            WorkDTO workDTO = workService.getWorkById(id);
            workService.certifyWork(workDTO.getWorkId());

            LogContextUtil.set(id);

            return ApiResponse.success("确权成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @Log(module = "作品", operationType = OperationTypeEnum.DOWNLOAD, description = "下载作品", targetType = "work")
    @GetMapping("/{id}/download")
    public void downloadMyWork(@PathVariable Long id, HttpServletResponse response) {
        try {
            User currentUser = UserUtil.get();
            if (currentUser == null) {
                response.setStatus(com.blockchain.aigc.enums.ResultEnum.NO_LOGIN.getCode());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"未登录！\"}");
                return;
            }
            Work work = workService.getWorkEntityById(id);
            if (work == null) {
                response.setStatus(404);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":404,\"message\":\"作品不存在\"}");
                return;
            }
            if (!work.getUserId().equals(currentUser.getId())) {
                response.setStatus(com.blockchain.aigc.enums.ResultEnum.NO_PERMISSION.getCode());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"无权限!\"}");
                return;
            }
            String path = work.getMinioPath();
            String objectName = path.contains("/") ? path.substring(path.indexOf("/") + 1) : path;
            try (InputStream in = minioService.downloadFile(objectName);
                 OutputStream out = response.getOutputStream()) {
                String fileName = work.getFileName() == null ? "work" : work.getFileName();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
                response.setContentType("application/octet-stream");
                if (work.getFileSize() != null) {
                    response.setHeader("Content-Length", String.valueOf(work.getFileSize()));
                }
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

            LogContextUtil.set(id);
        } catch (Exception e) {
            try {
                response.setStatus(500);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":500,\"message\":\"文件下载失败\"}");
            } catch (Exception ignored) {}
        }
    }

    @GetMapping("/admin/{id}/download")
    public void downloadWorkAsAdmin(@PathVariable Long id, HttpServletResponse response) {
        try {
            User admin = UserUtil.get();
            if (admin == null) {
                response.setStatus(com.blockchain.aigc.enums.ResultEnum.NO_LOGIN.getCode());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"未登录！\"}");
                return;
            }
            if (admin.getIsAdmin() == null || admin.getIsAdmin() != 1) {
                response.setStatus(com.blockchain.aigc.enums.ResultEnum.NO_PERMISSION.getCode());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"无权限!\"}");
                return;
            }
            Work work = workService.getWorkEntityById(id);
            if (work == null) {
                response.setStatus(404);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":404,\"message\":\"作品不存在\"}");
                return;
            }
            String path = work.getMinioPath();
            String objectName = path.contains("/") ? path.substring(path.indexOf("/") + 1) : path;
            try (InputStream in = minioService.downloadFile(objectName);
                 OutputStream out = response.getOutputStream()) {
                String fileName = work.getFileName() == null ? "work" : work.getFileName();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
                response.setContentType("application/octet-stream");
                if (work.getFileSize() != null) {
                    response.setHeader("Content-Length", String.valueOf(work.getFileSize()));
                }
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

            LogContextUtil.set(id);
        } catch (Exception e) {
            try {
                response.setStatus(500);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":500,\"message\":\"文件下载失败\"}");
            } catch (Exception ignored) {}
        }
    }
}
