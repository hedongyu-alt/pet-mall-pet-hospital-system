package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.Hospital;
import com.notmaker.service.HospitalService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 宠物医院Controller
 */
@RestController
@RequestMapping("/api/hospital")
public class HospitalController {
    
    @Autowired
    private HospitalService hospitalService;
    
    /**
     * 分页查询宠物医院列表
     * @param page 当前页
     * @param size 每页大小
     * @param name 医院名称
     * @param services 服务项目
     * @param address 医院地址
     * @return 查询结果
     */
    @GetMapping("/list")
    public Map<String, Object> listHospitals(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String services,
            @RequestParam(required = false) String address) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<Hospital> pageParam = new Page<>(page, size);
            IPage<Hospital> pageResult = hospitalService.getHospitalPage(pageParam, name, services, address);
            
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
     * 获取宠物医院详情
     * @param id 医院ID
     * @return 医院信息
     */
    @GetMapping("/detail/{id}")
    public Map<String, Object> getHospitalDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Hospital hospital = hospitalService.getHospitalById(id);
            if (hospital != null) {
                result.put("success", true);
                result.put("data", hospital);
            } else {
                result.put("success", false);
                result.put("message", "医院不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 新增宠物医院
     * @param hospital 医院信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Map<String, Object> addHospital(@RequestBody Hospital hospital) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (hospital.getName() == null || hospital.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "医院名称不能为空");
                return result;
            }
            
            if (hospital.getAddress() == null || hospital.getAddress().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "医院地址不能为空");
                return result;
            }
            
            if (hospital.getPhone() == null || hospital.getPhone().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "联系电话不能为空");
                return result;
            }
            
            if (hospital.getBusinessHours() == null || hospital.getBusinessHours().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "营业时间不能为空");
                return result;
            }
            
            boolean success = hospitalService.addHospital(hospital);
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
     * 更新宠物医院
     * @param hospital 医院信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Map<String, Object> updateHospital(@RequestBody Hospital hospital) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            if (hospital.getId() == null) {
                result.put("success", false);
                result.put("message", "医院ID不能为空");
                return result;
            }
            
            if (hospital.getName() == null || hospital.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "医院名称不能为空");
                return result;
            }
            
            if (hospital.getAddress() == null || hospital.getAddress().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "医院地址不能为空");
                return result;
            }
            
            if (hospital.getPhone() == null || hospital.getPhone().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "联系电话不能为空");
                return result;
            }
            
            if (hospital.getBusinessHours() == null || hospital.getBusinessHours().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "营业时间不能为空");
                return result;
            }
            
            boolean success = hospitalService.updateHospital(hospital);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 删除宠物医院
     * @param id 医院ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteHospital(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = hospitalService.deleteHospital(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 导出Excel
     * @param response HttpServletResponse
     * @param name 医院名称
     * @param services 服务项目
     * @param address 医院地址
     */
    @GetMapping("/export")
    public void exportExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String services,
            @RequestParam(required = false) String address) {
        
        try {
            Map<String, Object> exportData = hospitalService.exportExcel(name, services, address);
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("宠物医院列表");
            
            // 获取数据
            String[][] data = (String[][]) ((java.util.List<?>) exportData.get("data"))
                    .toArray(new String[0][]);
            
            // 创建单元格样式（表头）
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // 写入数据
            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data[i][j]);
                    
                    // 表头应用样式
                    if (i == 0) {
                        cell.setCellStyle(headerStyle);
                    }
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < data[0].length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 设置响应头
            String filename = "宠物医院列表_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + 
                    new String(filename.getBytes("UTF-8"), "ISO-8859-1"));
            
            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

