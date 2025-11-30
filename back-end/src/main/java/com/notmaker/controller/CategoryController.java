package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Category;
import com.notmaker.service.CategoryService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Controller
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 用户端获取所有商品分类列表
     * @return 分类列表
     */
    @GetMapping("/all")
    public Map<String, Object> getAllCategories() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Category> categories = categoryService.getAllCategories();
            result.put("success", true);
            result.put("data", categories);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端分页查询商品分类列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 分类名称
     * @return 查询结果
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Category> pageResult = categoryService.adminQueryCategories(page, size, name);
            
            // 手动构建分页数据，确保字段名称正确
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageResult.getRecords());
            pageData.put("total", pageResult.getTotal());
            pageData.put("size", pageResult.getSize());
            pageData.put("current", pageResult.getCurrent());
            pageData.put("pages", pageResult.getPages());
            
            result.put("success", true);
            result.put("data", pageData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端获取商品分类详情
     * @param id 商品分类ID
     * @return 商品分类信息
     */
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> adminGetCategoryDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            result.put("success", true);
            result.put("data", category);
        } else {
            result.put("success", false);
            result.put("message", "商品分类不存在");
        }
        
        return result;
    }
    
    /**
     * 管理端新增商品分类
     * @param categoryRequest 商品分类信息
     * @return 新增结果
     */
    @PostMapping("/admin/add")
    public Map<String, Object> adminAddCategory(@RequestBody Map<String, Object> categoryRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            String name = (String) categoryRequest.get("name");
            
            if (name == null || name.isEmpty()) {
                result.put("success", false);
                result.put("message", "分类名称不能为空");
                return result;
            }
            
            // 创建商品分类对象
            Category category = new Category();
            category.setName(name);
            category.setDescription((String) categoryRequest.get("description"));
            
            // 调用服务新增商品分类
            boolean success = categoryService.adminAddCategory(category);
            
            if (success) {
                result.put("success", true);
                result.put("message", "新增成功");
            } else {
                result.put("success", false);
                result.put("message", "分类名称已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端更新商品分类
     * @param categoryRequest 商品分类信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateCategory(@RequestBody Map<String, Object> categoryRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取商品分类ID
            Object idObj = categoryRequest.get("id");
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "商品分类ID不能为空");
                return result;
            }
            
            Long categoryId = Long.valueOf(idObj.toString());
            
            // 创建商品分类对象
            Category category = new Category();
            category.setId(categoryId);
            category.setName((String) categoryRequest.get("name"));
            category.setDescription((String) categoryRequest.get("description"));
            
            // 调用服务更新商品分类
            boolean success = categoryService.adminUpdateCategory(category);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败，商品分类不存在或分类名称已被占用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端删除商品分类
     * @param id 商品分类ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{id}")
    public Map<String, Object> adminDeleteCategory(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = categoryService.adminDeleteCategory(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败，商品分类不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端导出商品分类Excel
     * @param name 分类名称
     * @param response HttpServletResponse
     */
    @GetMapping("/admin/export")
    public void adminExportCategories(
            @RequestParam(required = false) String name,
            HttpServletResponse response) {
        
        try {
            // 查询所有符合条件的商品分类
            List<Category> categories = categoryService.adminQueryAllCategories(name);
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("商品分类列表");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"分类ID", "分类名称", "分类描述", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            
            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (Category category : categories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(category.getId());
                row.createCell(1).setCellValue(category.getName());
                row.createCell(2).setCellValue(category.getDescription());
                row.createCell(3).setCellValue(category.getCreateTime() != null ? dateFormat.format(category.getCreateTime()) : "");
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = "商品分类列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            
            // 输出到响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

