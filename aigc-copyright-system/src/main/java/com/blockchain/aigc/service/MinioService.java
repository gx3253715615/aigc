package com.blockchain.aigc.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {
    
    @Autowired
    private MinioClient minioClient;
    
    @Value("${minio.bucket-name}")
    private String bucketName;
    
    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file, String objectName) {
        try {
            // 确保bucket存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
            
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取文件访问URL
     */
    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(7, TimeUnit.DAYS)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("获取文件URL失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除文件
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 下载文件
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("下载文件失败: " + e.getMessage(), e);
        }
    }
}
