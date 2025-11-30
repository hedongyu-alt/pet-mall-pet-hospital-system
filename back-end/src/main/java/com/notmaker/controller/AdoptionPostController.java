package com.notmaker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.AdoptionPost;
import com.notmaker.service.AdoptionPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宠物领养帖子Controller
 */
@RestController
@RequestMapping("/api/adoption")
public class AdoptionPostController {
    
    @Autowired
    private AdoptionPostService adoptionPostService;
    
    /**
     * 用户端分页查询领养帖子列表（只显示"寻找中"的帖子）
     */
    @GetMapping("/user/list")
    public Map<String, Object> userListAdoptionPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String petGender,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Long currentUserId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = adoptionPostService.userQueryAdoptionPosts(
                    page, size, petType, petGender, breed, publisher, currentUserId);
            
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
     * 用户端获取领养帖子详情
     */
    @GetMapping("/user/detail/{id}")
    public Map<String, Object> userGetAdoptionPostDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> postDetail = adoptionPostService.userGetAdoptionPostDetail(id);
            if (postDetail != null) {
                result.put("success", true);
                result.put("data", postDetail);
            } else {
                result.put("success", false);
                result.put("message", "帖子不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户端发布领养帖子
     */
    @PostMapping("/user/add")
    public Map<String, Object> userAddAdoptionPost(@RequestBody Map<String, Object> postRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            Object userIdObj = postRequest.get("userId");
            String title = (String) postRequest.get("title");
            String petType = (String) postRequest.get("petType");
            String petGender = (String) postRequest.get("petGender");
            String petName = (String) postRequest.get("petName");
            String breed = (String) postRequest.get("breed");
            
            if (userIdObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (title == null || title.isEmpty()) {
                result.put("success", false);
                result.put("message", "标题不能为空");
                return result;
            }
            
            if (petType == null || petType.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物类型不能为空");
                return result;
            }
            
            if (petGender == null || petGender.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物性别不能为空");
                return result;
            }
            
            if (petName == null || petName.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物名称不能为空");
                return result;
            }
            
            if (breed == null || breed.isEmpty()) {
                result.put("success", false);
                result.put("message", "品种不能为空");
                return result;
            }
            
            // 创建帖子对象
            AdoptionPost post = new AdoptionPost();
            post.setUserId(Long.valueOf(userIdObj.toString()));
            post.setTitle(title);
            post.setDescription((String) postRequest.get("description"));
            post.setPetType(petType);
            post.setPetGender(petGender);
            post.setPetName(petName);
            post.setBreed(breed);
            post.setVaccineStatus((String) postRequest.get("vaccineStatus"));
            post.setPhotos((String) postRequest.get("photos"));
            post.setAdoptionRequirement((String) postRequest.get("adoptionRequirement"));
            post.setLocation((String) postRequest.get("location"));
            post.setOwnerInfo((String) postRequest.get("ownerInfo"));
            post.setStatus("寻找中"); // 默认状态
            
            boolean success = adoptionPostService.userAddAdoptionPost(post);
            
            if (success) {
                result.put("success", true);
                result.put("message", "发布成功");
            } else {
                result.put("success", false);
                result.put("message", "发布失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发布失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户端更新领养帖子（只能更新自己发布的）
     */
    @PostMapping("/user/update")
    public Map<String, Object> userUpdateAdoptionPost(@RequestBody Map<String, Object> postRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object idObj = postRequest.get("id");
            Object currentUserIdObj = postRequest.get("currentUserId");
            
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "帖子ID不能为空");
                return result;
            }
            
            if (currentUserIdObj == null) {
                result.put("success", false);
                result.put("message", "当前用户ID不能为空");
                return result;
            }
            
            Long postId = Long.valueOf(idObj.toString());
            Long currentUserId = Long.valueOf(currentUserIdObj.toString());
            
            // 参数校验
            String title = (String) postRequest.get("title");
            String petType = (String) postRequest.get("petType");
            String petGender = (String) postRequest.get("petGender");
            String petName = (String) postRequest.get("petName");
            String breed = (String) postRequest.get("breed");
            
            if (title == null || title.isEmpty()) {
                result.put("success", false);
                result.put("message", "标题不能为空");
                return result;
            }
            
            if (petType == null || petType.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物类型不能为空");
                return result;
            }
            
            if (petGender == null || petGender.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物性别不能为空");
                return result;
            }
            
            if (petName == null || petName.isEmpty()) {
                result.put("success", false);
                result.put("message", "宠物名称不能为空");
                return result;
            }
            
            if (breed == null || breed.isEmpty()) {
                result.put("success", false);
                result.put("message", "品种不能为空");
                return result;
            }
            
            // 创建帖子对象
            AdoptionPost post = new AdoptionPost();
            post.setId(postId);
            post.setTitle(title);
            post.setDescription((String) postRequest.get("description"));
            post.setPetType(petType);
            post.setPetGender(petGender);
            post.setPetName(petName);
            post.setBreed(breed);
            post.setVaccineStatus((String) postRequest.get("vaccineStatus"));
            post.setPhotos((String) postRequest.get("photos"));
            post.setAdoptionRequirement((String) postRequest.get("adoptionRequirement"));
            post.setLocation((String) postRequest.get("location"));
            post.setOwnerInfo((String) postRequest.get("ownerInfo"));
            post.setStatus((String) postRequest.get("status"));
            
            boolean success = adoptionPostService.userUpdateAdoptionPost(post, currentUserId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败，帖子不存在或无权限修改");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 用户端修改领养帖子状态（只能修改自己发布的）
     */
    @PostMapping("/user/updateStatus/{id}")
    public Map<String, Object> userUpdateAdoptionPostStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> statusRequest) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String status = (String) statusRequest.get("status");
            Object currentUserIdObj = statusRequest.get("currentUserId");
            
            if (status == null || status.isEmpty()) {
                result.put("success", false);
                result.put("message", "状态不能为空");
                return result;
            }
            
            if (currentUserIdObj == null) {
                result.put("success", false);
                result.put("message", "当前用户ID不能为空");
                return result;
            }
            
            Long currentUserId = Long.valueOf(currentUserIdObj.toString());
            
            boolean success = adoptionPostService.userUpdateAdoptionPostStatus(id, status, currentUserId);
            
            if (success) {
                result.put("success", true);
                result.put("message", "状态修改成功");
            } else {
                result.put("success", false);
                result.put("message", "状态修改失败，帖子不存在或无权限修改");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "状态修改失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端分页查询领养帖子列表
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListAdoptionPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String petGender,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<Map<String, Object>> pageResult = adoptionPostService.adminQueryAdoptionPosts(
                    page, size, petType, petGender, breed, status);
            
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
     * 管理端获取领养帖子详情
     */
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> adminGetAdoptionPostDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> postDetail = adoptionPostService.adminGetAdoptionPostDetail(id);
            if (postDetail != null) {
                result.put("success", true);
                result.put("data", postDetail);
            } else {
                result.put("success", false);
                result.put("message", "帖子不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端删除领养帖子
     */
    @PostMapping("/admin/delete/{id}")
    public Map<String, Object> adminDeleteAdoptionPost(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = adoptionPostService.adminDeleteAdoptionPost(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败，帖子不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 管理端更新领养帖子
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateAdoptionPost(@RequestBody AdoptionPost post) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = adoptionPostService.adminUpdateAdoptionPost(post);
            
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
     * 管理端导出领养帖子Excel
     * @param petType 宠物类型
     * @param petGender 宠物性别
     * @param breed 品种
     * @param status 状态
     * @param response HTTP响应
     */
    @GetMapping("/admin/export")
    public void exportAdoption(
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String petGender,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String status,
            javax.servlet.http.HttpServletResponse response) {
        
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=adoption_" + System.currentTimeMillis() + ".xls");
            
            // 获取所有符合条件的领养帖子数据（不分页）
            IPage<Map<String, Object>> pageResult = adoptionPostService.adminQueryAdoptionPosts(
                    1, 100000, petType, petGender, breed, status);
            List<Map<String, Object>> adoptionList = pageResult.getRecords();
            
            // 构建Excel内容（简单的HTML表格格式）
            StringBuilder sb = new StringBuilder();
            sb.append("<html><head><meta charset='UTF-8'></head><body>");
            sb.append("<table border='1'>");
            sb.append("<tr>");
            sb.append("<th>帖子ID</th>");
            sb.append("<th>标题</th>");
            sb.append("<th>宠物类型</th>");
            sb.append("<th>宠物性别</th>");
            sb.append("<th>宠物名称</th>");
            sb.append("<th>品种</th>");
            sb.append("<th>地理位置</th>");
            sb.append("<th>状态</th>");
            sb.append("<th>发布者昵称</th>");
            sb.append("<th>发布者手机</th>");
            sb.append("<th>发布时间</th>");
            sb.append("</tr>");
            
            for (Map<String, Object> adoption : adoptionList) {
                sb.append("<tr>");
                sb.append("<td>").append(adoption.get("id") != null ? adoption.get("id") : "").append("</td>");
                sb.append("<td>").append(adoption.get("title") != null ? adoption.get("title") : "").append("</td>");
                sb.append("<td>").append(adoption.get("petType") != null ? adoption.get("petType") : "").append("</td>");
                sb.append("<td>").append(adoption.get("petGender") != null ? adoption.get("petGender") : "").append("</td>");
                sb.append("<td>").append(adoption.get("petName") != null ? adoption.get("petName") : "").append("</td>");
                sb.append("<td>").append(adoption.get("breed") != null ? adoption.get("breed") : "").append("</td>");
                sb.append("<td>").append(adoption.get("location") != null ? adoption.get("location") : "").append("</td>");
                sb.append("<td>").append(adoption.get("status") != null ? adoption.get("status") : "").append("</td>");
                sb.append("<td>").append(adoption.get("userNickname") != null ? adoption.get("userNickname") : "").append("</td>");
                sb.append("<td>").append(adoption.get("userPhone") != null ? adoption.get("userPhone") : "").append("</td>");
                sb.append("<td>").append(adoption.get("createTime") != null ? adoption.get("createTime") : "").append("</td>");
                sb.append("</tr>");
            }
            
            sb.append("</table>");
            sb.append("</body></html>");
            
            // 写入响应流
            response.getWriter().write(sb.toString());
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

