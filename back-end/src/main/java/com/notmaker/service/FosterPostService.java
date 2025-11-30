package com.notmaker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notmaker.entity.FosterPost;

import java.util.Map;

/**
 * 寻找寄养帖子Service接口
 */
public interface FosterPostService {
    
    /**
     * 用户端分页查询寄养帖子列表（只显示"寻找中"的帖子，包含用户信息）
     * @param page 当前页
     * @param size 每页大小
     * @param petType 宠物类型
     * @param petGender 宠物性别
     * @param breed 品种
     * @param publisher 发布者（my：我发布的，other：他人发布的）
     * @param currentUserId 当前用户ID
     * @return 分页结果（包含用户信息的Map列表）
     */
    IPage<Map<String, Object>> userQueryFosterPosts(int page, int size, String petType, 
                                                       String petGender, String breed, String publisher, Long currentUserId);
    
    /**
     * 用户端获取寄养帖子详情（包含用户信息）
     * @param id 帖子ID
     * @return 帖子详情Map（包含用户信息）
     */
    Map<String, Object> userGetFosterPostDetail(Long id);
    
    /**
     * 用户端发布寄养帖子
     * @param fosterPost 帖子对象
     * @return 发布结果
     */
    boolean userAddFosterPost(FosterPost fosterPost);
    
    /**
     * 用户端更新寄养帖子（只能更新自己发布的）
     * @param fosterPost 帖子对象
     * @param currentUserId 当前用户ID
     * @return 更新结果
     */
    boolean userUpdateFosterPost(FosterPost fosterPost, Long currentUserId);
    
    /**
     * 用户端修改寄养帖子状态（只能修改自己发布的）
     * @param id 帖子ID
     * @param status 状态：寻找中、已结束
     * @param currentUserId 当前用户ID
     * @return 修改结果
     */
    boolean userUpdateFosterPostStatus(Long id, String status, Long currentUserId);
    
    /**
     * 根据ID获取寄养帖子
     * @param id 帖子ID
     * @return 帖子对象
     */
    FosterPost getFosterPostById(Long id);
    
    /**
     * 管理端分页查询寄养帖子列表（包含用户信息，所有状态）
     * @param page 当前页
     * @param size 每页大小
     * @param petType 宠物类型
     * @param petGender 宠物性别
     * @param breed 品种
     * @param status 状态
     * @return 分页结果（包含用户信息的Map列表）
     */
    IPage<Map<String, Object>> adminQueryFosterPosts(int page, int size, String petType, 
                                                        String petGender, String breed, String status);
    
    /**
     * 管理端获取寄养帖子详情（包含用户信息）
     * @param id 帖子ID
     * @return 帖子详情Map（包含用户信息）
     */
    Map<String, Object> adminGetFosterPostDetail(Long id);
    
    /**
     * 管理端删除寄养帖子
     * @param id 帖子ID
     * @return 删除结果
     */
    boolean adminDeleteFosterPost(Long id);
    
    /**
     * 管理端更新寄养帖子
     * @param fosterPost 帖子对象
     * @return 更新结果
     */
    boolean adminUpdateFosterPost(FosterPost fosterPost);
}

