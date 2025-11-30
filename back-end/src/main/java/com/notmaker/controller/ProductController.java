package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Product;
import com.notmaker.service.ProductService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品Controller
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 管理端分页查询商品列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param status 状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 查询结果
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Product> pageResult = productService.adminQueryProducts(page, size, name, 
                    brand, categoryId, petType, status, minPrice, maxPrice);
            
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
     * 管理端获取商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> adminGetProductDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        Product product = productService.getProductById(id);
        if (product != null) {
            result.put("success", true);
            result.put("data", product);
        } else {
            result.put("success", false);
            result.put("message", "商品不存在");
        }
        
        return result;
    }
    
    /**
     * 管理端新增商品
     * @param productRequest 商品信息
     * @return 新增结果
     */
    @PostMapping("/admin/add")
    public Map<String, Object> adminAddProduct(@RequestBody Map<String, Object> productRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            String name = (String) productRequest.get("name");
            Object categoryIdObj = productRequest.get("categoryId");
            String petType = (String) productRequest.get("petType");
            Object priceObj = productRequest.get("price");
            
            if (name == null || name.isEmpty()) {
                result.put("success", false);
                result.put("message", "商品名称不能为空");
                return result;
            }
            
            if (categoryIdObj == null) {
                result.put("success", false);
                result.put("message", "商品分类不能为空");
                return result;
            }
            
            if (petType == null || petType.isEmpty()) {
                result.put("success", false);
                result.put("message", "适用宠物类型不能为空");
                return result;
            }
            
            if (priceObj == null) {
                result.put("success", false);
                result.put("message", "价格不能为空");
                return result;
            }
            
            // 创建商品对象
            Product product = new Product();
            product.setImages((String) productRequest.get("images"));
            product.setName(name);
            product.setCategoryId(Long.valueOf(categoryIdObj.toString()));
            product.setDescription((String) productRequest.get("description"));
            product.setBrand((String) productRequest.get("brand"));
            product.setPetType(petType);
            product.setPrice(new BigDecimal(priceObj.toString()));
            product.setStatus((String) productRequest.get("status"));
            
            // 调用服务新增商品
            boolean success = productService.adminAddProduct(product);
            
            if (success) {
                result.put("success", true);
                result.put("message", "新增成功");
            } else {
                result.put("success", false);
                result.put("message", "新增失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端更新商品
     * @param productRequest 商品信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateProduct(@RequestBody Map<String, Object> productRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取商品ID
            Object idObj = productRequest.get("id");
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "商品ID不能为空");
                return result;
            }
            
            Long productId = Long.valueOf(idObj.toString());
            
            // 参数校验
            String name = (String) productRequest.get("name");
            Object categoryIdObj = productRequest.get("categoryId");
            String petType = (String) productRequest.get("petType");
            Object priceObj = productRequest.get("price");
            
            if (name == null || name.isEmpty()) {
                result.put("success", false);
                result.put("message", "商品名称不能为空");
                return result;
            }
            
            if (categoryIdObj == null) {
                result.put("success", false);
                result.put("message", "商品分类不能为空");
                return result;
            }
            
            if (petType == null || petType.isEmpty()) {
                result.put("success", false);
                result.put("message", "适用宠物类型不能为空");
                return result;
            }
            
            if (priceObj == null) {
                result.put("success", false);
                result.put("message", "价格不能为空");
                return result;
            }
            
            // 创建商品对象
            Product product = new Product();
            product.setId(productId);
            product.setImages((String) productRequest.get("images"));
            product.setName(name);
            product.setCategoryId(Long.valueOf(categoryIdObj.toString()));
            product.setDescription((String) productRequest.get("description"));
            product.setBrand((String) productRequest.get("brand"));
            product.setPetType(petType);
            product.setPrice(new BigDecimal(priceObj.toString()));
            product.setStatus((String) productRequest.get("status"));
            
            // 调用服务更新商品
            boolean success = productService.adminUpdateProduct(product);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败，商品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端删除商品
     * @param id 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{id}")
    public Map<String, Object> adminDeleteProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = productService.adminDeleteProduct(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败，商品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端修改商品状态（上下架）
     * @param id 商品ID
     * @param statusRequest 状态信息
     * @return 修改结果
     */
    @PostMapping("/admin/updateStatus/{id}")
    public Map<String, Object> adminUpdateProductStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusRequest) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String status = (String) statusRequest.get("status");
            
            if (status == null || status.isEmpty()) {
                result.put("success", false);
                result.put("message", "状态不能为空");
                return result;
            }
            
            boolean success = productService.adminUpdateProductStatus(id, status);
            
            if (success) {
                result.put("success", true);
                result.put("message", "状态修改成功");
            } else {
                result.put("success", false);
                result.put("message", "状态修改失败，商品不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "状态修改失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端导出商品Excel
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param status 状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param response HttpServletResponse
     */
    @GetMapping("/admin/export")
    public void adminExportProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            HttpServletResponse response) {
        
        try {
            // 查询所有符合条件的商品
            List<Product> products = productService.adminQueryAllProducts(name, brand, 
                    categoryId, petType, status, minPrice, maxPrice);
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("商品列表");
            
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
            String[] headers = {"商品ID", "商品名称", "商品分类ID", "品牌", "适用宠物类型", "价格", "状态", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            
            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getCategoryId());
                row.createCell(3).setCellValue(product.getBrand());
                row.createCell(4).setCellValue(product.getPetType());
                row.createCell(5).setCellValue(product.getPrice() != null ? product.getPrice().doubleValue() : 0);
                row.createCell(6).setCellValue(product.getStatus());
                row.createCell(7).setCellValue(product.getCreateTime() != null ? dateFormat.format(product.getCreateTime()) : "");
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = "商品列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            
            // 输出到响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 用户端分页查询商品列表（只显示上架商品，包含分类名称）
     * @param page 当前页
     * @param size 每页大小
     * @param name 商品名称
     * @param brand 品牌
     * @param categoryId 商品分类ID
     * @param petType 适用宠物类型
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 查询结果
     */
    @GetMapping("/user/list")
    public Map<String, Object> userListProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 用户端只查询上架商品
            IPage<Map<String, Object>> pageResult = productService.userQueryProducts(page, size, name, 
                    brand, categoryId, petType, minPrice, maxPrice);
            
            // 构建分页数据
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
     * 用户端获取商品详情（只显示上架商品，包含分类名称）
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/user/detail/{id}")
    public Map<String, Object> userGetProductDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> productDetail = productService.userGetProductDetail(id);
            if (productDetail != null) {
                result.put("success", true);
                result.put("data", productDetail);
            } else {
                result.put("success", false);
                result.put("message", "商品不存在或已下架");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
}

