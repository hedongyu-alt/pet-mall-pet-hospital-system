package com.notmaker.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.User;
import com.notmaker.service.UserService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录（web端使用，需要验证码）
     * @param loginRequest 登录请求参数
     * @param session HttpSession
     * @return 登录结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String role = loginRequest.get("role");
        String captcha = loginRequest.get("captcha");
        
        // 参数校验
        if (username == null || username.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        
        if (password == null || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        
        if (role == null || role.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择登录身份");
            return result;
        }
        
        if (captcha == null || captcha.isEmpty()) {
            result.put("success", false);
            result.put("message", "验证码不能为空");
            return result;
        }
        
        // 验证验证码
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            result.put("success", false);
            result.put("message", "验证码错误");
            return result;
        }
        
        // 验证码使用后立即清除
        session.removeAttribute("captcha");
        
        // 先查询用户是否存在
        QueryWrapper<User> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("username", username);
        checkWrapper.eq("role", role);
        User existUser = userService.getOne(checkWrapper);
        
        // 如果用户存在，检查状态
        if (existUser != null && "禁用".equals(existUser.getStatus())) {
            result.put("success", false);
            result.put("message", "该账号已被禁用，无法登录");
            return result;
        }
        
        // 调用登录服务
        User user = userService.login(username, password, role);
        
        if (user != null) {
            // 登录成功，移除密码字段
            user.setPassword(null);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户名、密码或身份错误");
        }
        
        return result;
    }

    /**
     * 微信小程序用户登录（不需要验证码）
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/wechat/login")
    public Map<String, Object> wechatLogin(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> result = new HashMap<>();
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        String role = loginRequest.get("role");
        
        // 参数校验
        if (username == null || username.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        
        if (password == null || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        
        if (role == null || role.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择登录身份");
            return result;
        }
        
        // 先查询用户是否存在
        QueryWrapper<User> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("username", username);
        checkWrapper.eq("role", role);
        User existUser = userService.getOne(checkWrapper);
        
        // 如果用户存在，检查状态
        if (existUser != null && "禁用".equals(existUser.getStatus())) {
            result.put("success", false);
            result.put("message", "该账号已被禁用，无法登录");
            return result;
        }
        
        // 调用登录服务
        User user = userService.login(username, password, role);
        
        if (user != null) {
            // 登录成功，移除密码字段
            user.setPassword(null);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户名、密码或身份错误");
        }
        
        return result;
    }

    /**
     * 微信小程序用户注册（不需要验证码）
     * @param registerRequest 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/wechat/register")
    public Map<String, Object> wechatRegister(@RequestBody Map<String, String> registerRequest) {
        Map<String, Object> result = new HashMap<>();
        
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String nickname = registerRequest.get("nickname");
        String phone = registerRequest.get("phone");
        String email = registerRequest.get("email");
        
        // 参数校验
        if (username == null || username.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        
        if (password == null || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        
        if (nickname == null || nickname.isEmpty()) {
            result.put("success", false);
            result.put("message", "昵称不能为空");
            return result;
        }
        
        // 检查用户名是否已存在
        if (userService.checkUsernameExists(username)) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setEmail(email);
        
        // 调用注册服务
        boolean success = userService.register(user);
        
        if (success) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }
        
        return result;
    }

    /**
     * 用户注册（web端使用，需要验证码）
     * @param registerRequest 注册请求参数
     * @param session HttpSession
     * @return 注册结果
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> registerRequest, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String nickname = registerRequest.get("nickname");
        String phone = registerRequest.get("phone");
        String email = registerRequest.get("email");
        String captcha = registerRequest.get("captcha");
        
        // 参数校验
        if (username == null || username.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户名不能为空");
            return result;
        }
        
        if (password == null || password.isEmpty()) {
            result.put("success", false);
            result.put("message", "密码不能为空");
            return result;
        }
        
        if (nickname == null || nickname.isEmpty()) {
            result.put("success", false);
            result.put("message", "昵称不能为空");
            return result;
        }
        
        if (captcha == null || captcha.isEmpty()) {
            result.put("success", false);
            result.put("message", "验证码不能为空");
            return result;
        }
        
        // 验证验证码
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            result.put("success", false);
            result.put("message", "验证码错误");
            return result;
        }
        
        // 验证码使用后立即清除
        session.removeAttribute("captcha");
        
        // 检查用户名是否已存在
        if (userService.checkUsernameExists(username)) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setPhone(phone);
        user.setEmail(email);
        
        // 调用注册服务
        boolean success = userService.register(user);
        
        if (success) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }
        
        return result;
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/checkUsername")
    public Map<String, Object> checkUsername(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = userService.checkUsernameExists(username);
        result.put("exists", exists);
        return result;
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    public Map<String, Object> getUserInfo(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        User user = userService.getUserById(userId);
        if (user != null) {
            // 移除密码字段
            user.setPassword(null);
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        
        return result;
    }

    /**
     * 更新用户信息
     * @param updateRequest 更新请求参数
     * @return 更新结果
     */
    @PostMapping("/updateUserInfo")
    public Map<String, Object> updateUserInfo(@RequestBody Map<String, Object> updateRequest) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取参数
        Long userId = Long.valueOf(updateRequest.get("id").toString());
        String nickname = (String) updateRequest.get("nickname");
        String avatar = (String) updateRequest.get("avatar");
        String phone = (String) updateRequest.get("phone");
        String email = (String) updateRequest.get("email");
        
        // 参数校验
        if (userId == null) {
            result.put("success", false);
            result.put("message", "用户ID不能为空");
            return result;
        }
        
        if (nickname == null || nickname.isEmpty()) {
            result.put("success", false);
            result.put("message", "昵称不能为空");
            return result;
        }
        
        // 创建用户对象
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setPhone(phone);
        user.setEmail(email);
        
        // 调用更新服务
        boolean success = userService.updateUserInfo(user);
        
        if (success) {
            // 获取更新后的用户信息
            User updatedUser = userService.getUserById(userId);
            updatedUser.setPassword(null);
            
            result.put("success", true);
            result.put("message", "更新成功");
            result.put("data", updatedUser);
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
        }
        
        return result;
    }

    /**
     * 修改用户密码
     * @param passwordRequest 密码修改请求参数
     * @return 修改结果
     */
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> passwordRequest) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取参数
        String userIdStr = passwordRequest.get("userId");
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        
        // 参数校验
        if (userIdStr == null || userIdStr.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户ID不能为空");
            return result;
        }
        
        if (oldPassword == null || oldPassword.isEmpty()) {
            result.put("success", false);
            result.put("message", "旧密码不能为空");
            return result;
        }
        
        if (newPassword == null || newPassword.isEmpty()) {
            result.put("success", false);
            result.put("message", "新密码不能为空");
            return result;
        }
        
        Long userId = Long.valueOf(userIdStr);
        
        // 调用修改密码服务
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        
        if (success) {
            result.put("success", true);
            result.put("message", "密码修改成功");
        } else {
            result.put("success", false);
            result.put("message", "旧密码错误或用户不存在");
        }
        
        return result;
    }

    /**
     * 管理端分页查询用户列表
     * @param page 当前页
     * @param size 每页大小
     * @param nickname 昵称
     * @param phone 手机号
     * @param role 身份
     * @return 查询结果
     */
    @GetMapping("/admin/list")
    public Map<String, Object> adminListUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            IPage<User> pageResult = userService.adminQueryUsers(page, size, nickname, phone, role);
            
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
     * 管理端获取用户详情
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/admin/detail/{id}")
    public Map<String, Object> adminGetUserDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        User user = userService.getUserById(id);
        if (user != null) {
            user.setPassword(null);
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        
        return result;
    }

    /**
     * 管理端新增用户
     * @param userRequest 用户信息
     * @return 新增结果
     */
    @PostMapping("/admin/add")
    public Map<String, Object> adminAddUser(@RequestBody Map<String, Object> userRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 参数校验
            String username = (String) userRequest.get("username");
            String role = (String) userRequest.get("role");
            String nickname = (String) userRequest.get("nickname");
            
            if (username == null || username.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户名不能为空");
                return result;
            }
            
            if (role == null || role.isEmpty()) {
                result.put("success", false);
                result.put("message", "身份不能为空");
                return result;
            }
            
            if (nickname == null || nickname.isEmpty()) {
                result.put("success", false);
                result.put("message", "昵称不能为空");
                return result;
            }
            
            // 创建用户对象
            User user = new User();
            user.setUsername(username);
            // 新增用户时，默认密码为123456
            user.setPassword("123456");
            user.setRole(role);
            user.setNickname(nickname);
            user.setAvatar((String) userRequest.get("avatar"));
            user.setPhone((String) userRequest.get("phone"));
            user.setEmail((String) userRequest.get("email"));
            user.setStatus((String) userRequest.get("status"));
            
            // 调用服务新增用户
            boolean success = userService.adminAddUser(user);
            
            if (success) {
                result.put("success", true);
                result.put("message", "新增成功，默认密码为 123456");
            } else {
                result.put("success", false);
                result.put("message", "用户名已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "新增失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 管理端更新用户
     * @param userRequest 用户信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    public Map<String, Object> adminUpdateUser(@RequestBody Map<String, Object> userRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取用户ID
            Object idObj = userRequest.get("id");
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(idObj.toString());
            
            // 创建用户对象（编辑时不允许修改密码）
            User user = new User();
            user.setId(userId);
            user.setUsername((String) userRequest.get("username"));
            // 不设置密码，保持原密码不变
            user.setPassword(null);
            user.setRole((String) userRequest.get("role"));
            user.setNickname((String) userRequest.get("nickname"));
            user.setAvatar((String) userRequest.get("avatar"));
            user.setPhone((String) userRequest.get("phone"));
            user.setEmail((String) userRequest.get("email"));
            user.setStatus((String) userRequest.get("status"));
            
            // 调用服务更新用户
            boolean success = userService.adminUpdateUser(user);
            
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败，用户不存在或用户名已被占用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 管理端删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{id}")
    public Map<String, Object> adminDeleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = userService.adminDeleteUser(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败，用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 管理端切换用户状态
     * @param statusRequest 状态信息
     * @return 操作结果
     */
    @PostMapping("/admin/toggleStatus")
    public Map<String, Object> adminToggleUserStatus(@RequestBody Map<String, Object> statusRequest) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Object idObj = statusRequest.get("id");
            String status = (String) statusRequest.get("status");
            
            if (idObj == null) {
                result.put("success", false);
                result.put("message", "用户ID不能为空");
                return result;
            }
            
            if (status == null || status.isEmpty()) {
                result.put("success", false);
                result.put("message", "状态不能为空");
                return result;
            }
            
            Long userId = Long.valueOf(idObj.toString());
            
            boolean success = userService.adminToggleUserStatus(userId, status);
            
            if (success) {
                result.put("success", true);
                result.put("message", "操作成功");
            } else {
                result.put("success", false);
                result.put("message", "操作失败，用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 管理端导出用户Excel
     * @param nickname 昵称
     * @param phone 手机号
     * @param role 身份
     * @param response HttpServletResponse
     */
    @GetMapping("/admin/export")
    public void adminExportUsers(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role,
            HttpServletResponse response) {
        
        try {
            // 查询所有符合条件的用户
            List<User> users = userService.adminQueryAllUsers(nickname, phone, role);
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("用户列表");
            
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
            String[] headers = {"用户ID", "用户名", "昵称", "身份", "手机号", "邮箱", "状态", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }
            
            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getNickname());
                row.createCell(3).setCellValue(user.getRole());
                row.createCell(4).setCellValue(user.getPhone());
                row.createCell(5).setCellValue(user.getEmail());
                row.createCell(6).setCellValue(user.getStatus());
                row.createCell(7).setCellValue(user.getCreateTime() != null ? dateFormat.format(user.getCreateTime()) : "");
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = "用户列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            
            // 输出到响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

