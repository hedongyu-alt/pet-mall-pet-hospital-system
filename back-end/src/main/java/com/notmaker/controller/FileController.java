package com.notmaker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传Controller
 * 提供公共的文件上传接口
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    /**
     * 文件上传根路径（从配置文件读取）
     */
    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 文件访问URL前缀（从配置文件读取）
     */
    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    /**
     * 上传文件
     * @param file 上传的文件
     * @return 上传结果，包含完整的文件访问URL
     */
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        // 参数校验
        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择要上传的文件");
            return result;
        }
        
        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                result.put("success", false);
                result.put("message", "文件名不能为空");
                return result;
            }
            
            // 获取文件扩展名
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }
            
            // 生成按日期分类的目录结构：yyyy/MM/dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = sdf.format(new Date());
            
            // 创建完整的上传目录
            String fullUploadPath = uploadPath + datePath + "/";
            File uploadDir = new File(fullUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名：UUID + 扩展名
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
            
            // 保存文件
            File destFile = new File(fullUploadPath + fileName);
            file.transferTo(destFile);
            
            // 生成完整的访问URL
            String fileUrl = urlPrefix + datePath + "/" + fileName;
            
            result.put("success", true);
            result.put("message", "上传成功");
            result.put("data", fileUrl);
            
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "系统错误：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量上传文件
     * @param files 上传的文件数组
     * @return 上传结果，包含所有文件的访问URL
     */
    @PostMapping("/batchUpload")
    public Map<String, Object> batchUpload(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        
        // 参数校验
        if (files == null || files.length == 0) {
            result.put("success", false);
            result.put("message", "请选择要上传的文件");
            return result;
        }
        
        try {
            // 存储所有上传成功的文件URL
            String[] fileUrls = new String[files.length];
            
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                
                if (file.isEmpty()) {
                    continue;
                }
                
                // 获取原始文件名
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    continue;
                }
                
                // 获取文件扩展名
                String extension = "";
                int lastDotIndex = originalFilename.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    extension = originalFilename.substring(lastDotIndex);
                }
                
                // 生成按日期分类的目录结构
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String datePath = sdf.format(new Date());
                
                // 创建完整的上传目录
                String fullUploadPath = uploadPath + datePath + "/";
                File uploadDir = new File(fullUploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // 生成唯一文件名
                String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
                
                // 保存文件
                File destFile = new File(fullUploadPath + fileName);
                file.transferTo(destFile);
                
                // 生成完整的访问URL
                fileUrls[i] = urlPrefix + datePath + "/" + fileName;
            }
            
            result.put("success", true);
            result.put("message", "批量上传成功");
            result.put("urls", fileUrls);
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "批量上传失败：" + e.getMessage());
        }
        
        return result;
    }
}

