package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notmaker.entity.User;

import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param role 角色
     * @return 用户信息
     */
    User login(String username, String password, String role);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 是否注册成功
     */
    boolean register(User user);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(User user);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 管理端分页查询用户列表
     * @param page 当前页
     * @param size 每页大小
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param role 身份（可选）
     * @return 分页结果
     */
    IPage<User> adminQueryUsers(int page, int size, String nickname, String phone, String role);

    /**
     * 管理端新增用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean adminAddUser(User user);

    /**
     * 管理端更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean adminUpdateUser(User user);

    /**
     * 管理端删除用户
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean adminDeleteUser(Long userId);

    /**
     * 管理端切换用户状态（正常/禁用）
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean adminToggleUserStatus(Long userId, String status);

    /**
     * 管理端查询所有用户（用于导出Excel）
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param role 身份（可选）
     * @return 用户列表
     */
    List<User> adminQueryAllUsers(String nickname, String phone, String role);
}

