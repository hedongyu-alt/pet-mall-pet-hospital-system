package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notmaker.entity.User;
import com.notmaker.mapper.UserMapper;
import com.notmaker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     * @return 用户信息
     */
    @Override
    public User login(String username, String password, String role) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        queryWrapper.eq("role", role);
        
        // 查询用户
        User user = this.getOne(queryWrapper);
        
        // 检查用户是否存在
        if (user == null) {
            return null;
        }
        
        // 检查用户状态，如果是"禁用"则不允许登录
        if ("禁用".equals(user.getStatus())) {
            return null;
        }
        
        // 返回用户信息（密码将在Controller层移除）
        return user;
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 是否注册成功
     */
    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername())) {
            return false;
        }
        
        // 设置默认值
        user.setRole("普通用户");
        user.setStatus("正常");
        user.setCreateTime(new Date());
        
        // 如果没有设置头像，使用默认头像
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500");
        }
        
        // 保存用户信息
        return this.save(user);
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    @Override
    public boolean checkUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return this.count(queryWrapper) > 0;
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(Long userId) {
        return this.getById(userId);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateUserInfo(User user) {
        // 只更新允许修改的字段：昵称、头像、手机、邮箱
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setNickname(user.getNickname());
        updateUser.setAvatar(user.getAvatar());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        
        return this.updateById(updateUser);
    }

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 先查询用户
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }
        
        // 验证旧密码
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }
        
        // 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(newPassword);
        
        return this.updateById(updateUser);
    }

    /**
     * 管理端分页查询用户列表
     * @param page 当前页
     * @param size 每页大小
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param role 身份（可选）
     * @return 分页结果
     */
    @Override
    public IPage<User> adminQueryUsers(int page, int size, String nickname, String phone, String role) {
        // 创建分页对象
        Page<User> pageObj = new Page<>(page, size);
        
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        // 昵称模糊查询
        if (nickname != null && !nickname.isEmpty()) {
            queryWrapper.like("nickname", nickname);
        }
        
        // 手机号模糊查询
        if (phone != null && !phone.isEmpty()) {
            queryWrapper.like("phone", phone);
        }
        
        // 身份精确查询
        if (role != null && !role.isEmpty()) {
            queryWrapper.eq("role", role);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        // 执行分页查询
        IPage<User> result = this.page(pageObj, queryWrapper);
        
        // 移除密码字段
        result.getRecords().forEach(user -> user.setPassword(null));
        
        return result;
    }

    /**
     * 管理端新增用户
     * @param user 用户信息
     * @return 是否成功
     */
    @Override
    public boolean adminAddUser(User user) {
        // 检查用户名是否已存在
        if (checkUsernameExists(user.getUsername())) {
            return false;
        }
        
        // 设置默认值
        user.setCreateTime(new Date());
        if (user.getStatus() == null || user.getStatus().isEmpty()) {
            user.setStatus("正常");
        }
        
        // 如果没有设置头像，使用默认头像
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar("https://img1.baidu.com/it/u=1666852494,3843174075&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500");
        }
        
        // 保存用户信息
        return this.save(user);
    }

    /**
     * 管理端更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    @Override
    public boolean adminUpdateUser(User user) {
        // 检查用户是否存在
        User existUser = this.getById(user.getId());
        if (existUser == null) {
            return false;
        }
        
        // 如果修改了用户名，检查新用户名是否已被其他用户使用
        if (user.getUsername() != null && !user.getUsername().equals(existUser.getUsername())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", user.getUsername());
            queryWrapper.ne("id", user.getId());
            if (this.count(queryWrapper) > 0) {
                return false;
            }
        }
        
        // 更新用户信息（只更新非空字段，密码不允许在此处修改）
        User updateUser = new User();
        updateUser.setId(user.getId());
        
        if (user.getUsername() != null) {
            updateUser.setUsername(user.getUsername());
        }
        // 密码字段不更新，用户需通过修改密码功能单独修改
        // if (user.getPassword() != null) {
        //     updateUser.setPassword(user.getPassword());
        // }
        if (user.getRole() != null) {
            updateUser.setRole(user.getRole());
        }
        if (user.getNickname() != null) {
            updateUser.setNickname(user.getNickname());
        }
        if (user.getAvatar() != null) {
            updateUser.setAvatar(user.getAvatar());
        }
        if (user.getPhone() != null) {
            updateUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getStatus() != null) {
            updateUser.setStatus(user.getStatus());
        }
        
        return this.updateById(updateUser);
    }

    /**
     * 管理端删除用户
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    public boolean adminDeleteUser(Long userId) {
        // 检查用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }
        
        // 删除用户
        return this.removeById(userId);
    }

    /**
     * 管理端切换用户状态（正常/禁用）
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    @Override
    public boolean adminToggleUserStatus(Long userId, String status) {
        // 检查用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }
        
        // 更新状态
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(status);
        
        return this.updateById(updateUser);
    }

    /**
     * 管理端查询所有用户（用于导出Excel）
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param role 身份（可选）
     * @return 用户列表
     */
    @Override
    public List<User> adminQueryAllUsers(String nickname, String phone, String role) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        // 昵称模糊查询
        if (nickname != null && !nickname.isEmpty()) {
            queryWrapper.like("nickname", nickname);
        }
        
        // 手机号模糊查询
        if (phone != null && !phone.isEmpty()) {
            queryWrapper.like("phone", phone);
        }
        
        // 身份精确查询
        if (role != null && !role.isEmpty()) {
            queryWrapper.eq("role", role);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");
        
        // 查询所有符合条件的用户
        List<User> users = this.list(queryWrapper);
        
        // 移除密码字段
        users.forEach(user -> user.setPassword(null));
        
        return users;
    }
}

