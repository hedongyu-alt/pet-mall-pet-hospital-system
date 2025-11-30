package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.Insurance;
import com.notmaker.service.InsuranceService;
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
 * 宠物保险Controller
 */
@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {
    
    @Autowired
    private InsuranceService insuranceService;
    
    /**
     * 管理端分页查询保险列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 查询结果
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListInsurances(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Insurance> pageResult = insuranceService.adminQueryInsurances(page, size, name, minPrice, maxPrice);
            
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
     * 管理端获取保险详情
     * @param id 保险ID
     * @return 保险信息
     */
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> adminGetInsuranceDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance != null) {
            result.put("success", true);
            result.put("data", insurance);
        } else {
            result.put("success", false);
            result.put("message", "保险不存在");
        }
        
        return result;
    }
    
    /**
     * 管理端新增保险
     * @param insuranceRequest 保险信息
     * @return 新增结果
     */
    @PostMapping("/admin/add")
    public Map<String, Object> adminAddInsurance(@RequestBody Map<String, Object> insuranceRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            String name = (String) insuranceRequest.get("name");
            Object priceObj = insuranceRequest.get("price");
            
            if (name == null || name.isEmpty()) {
                result.put("success", false);
                result.put("message", "保险名称不能为空");
                return result;
            }
            
            if (priceObj == null) {
                result.put("success", false);
                result.put("message", "价格不能为空");
                return result;
            }
            
            // 创建保险对象
            Insurance insurance = new Insurance();
            insurance.setName(name);
            insurance.setDetail((String) insuranceRequest.get("detail"));
            insurance.setPrice(new BigDecimal(priceObj.toString()));
            insurance.setCoverage((String) insuranceRequest.get("coverage"));
            insurance.setClaimNotice((String) insuranceRequest.get("claimNotice"));
            insurance.setClaimLimit((String) insuranceRequest.get("claimLimit"));
            
            // 调用服务新增保险
            boolean success = insuranceService.adminAddInsurance(insurance);
            
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
     * 管理端更新保险
     * @param insuranceRequest 保险信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateInsurance(@RequestBody Map<String, Object> insuranceRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取保险ID
            Object idObj = insuranceRequest.get("id");
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "保险ID不能为空");
                return result;
            }
            
            Long insuranceId = Long.valueOf(idObj.toString());
            
            // 参数校验
            String name = (String) insuranceRequest.get("name");
            Object priceObj = insuranceRequest.get("price");
            
            if (name == null || name.isEmpty()) {
                result.put("success", false);
                result.put("message", "保险名称不能为空");
                return result;
            }
            
            if (priceObj == null) {
                result.put("success", false);
                result.put("message", "价格不能为空");
                return result;
            }
            
            // 创建保险对象
            Insurance insurance = new Insurance();
            insurance.setId(insuranceId);
            insurance.setName(name);
            insurance.setDetail((String) insuranceRequest.get("detail"));
            insurance.setPrice(new BigDecimal(priceObj.toString()));
            insurance.setCoverage((String) insuranceRequest.get("coverage"));
            insurance.setClaimNotice((String) insuranceRequest.get("claimNotice"));
            insurance.setClaimLimit((String) insuranceRequest.get("claimLimit"));
            
            // 调用服务更新保险
            boolean success = insuranceService.adminUpdateInsurance(insurance);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败，保险不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端删除保险
     * @param id 保险ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{id}")
    public Map<String, Object> adminDeleteInsurance(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = insuranceService.adminDeleteInsurance(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败，保险不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端导出保险Excel
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param response HttpServletResponse
     */
    @GetMapping("/admin/export")
    public void adminExportInsurances(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            HttpServletResponse response) {
        
        try {
            // 查询所有符合条件的保险
            List<Insurance> insurances = insuranceService.adminQueryAllInsurances(name, minPrice, maxPrice);
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("保险列表");
            
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
            String[] headers = {"保险ID", "保险名称", "价格/年", "保险范围", "理赔限额", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }
            
            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (Insurance insurance : insurances) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(insurance.getId());
                row.createCell(1).setCellValue(insurance.getName());
                row.createCell(2).setCellValue(insurance.getPrice() != null ? insurance.getPrice().doubleValue() : 0);
                row.createCell(3).setCellValue(insurance.getCoverage());
                row.createCell(4).setCellValue(insurance.getClaimLimit());
                row.createCell(5).setCellValue(insurance.getCreateTime() != null ? dateFormat.format(insurance.getCreateTime()) : "");
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = "保险列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            
            // 输出到响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 用户端分页查询保险列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 保险名称
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 查询结果
     */
    @GetMapping("/user/list")
    public Map<String, Object> userListInsurances(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Insurance> pageResult = insuranceService.adminQueryInsurances(page, size, name, minPrice, maxPrice);
            
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
     * 用户端获取保险详情
     * @param id 保险ID
     * @return 保险信息
     */
    @GetMapping("/user/detail/{id}")
    public Map<String, Object> userGetInsuranceDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        Insurance insurance = insuranceService.getInsuranceById(id);
        if (insurance != null) {
            result.put("success", true);
            result.put("data", insurance);
        } else {
            result.put("success", false);
            result.put("message", "保险不存在");
        }
        
        return result;
    }
}

