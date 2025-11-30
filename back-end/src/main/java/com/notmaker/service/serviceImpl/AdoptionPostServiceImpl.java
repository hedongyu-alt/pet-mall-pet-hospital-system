package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.AdoptionPost;
import com.notmaker.entity.User;
import com.notmaker.mapper.AdoptionPostMapper;
import com.notmaker.mapper.UserMapper;
import com.notmaker.service.AdoptionPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 宠物领养帖子Service实现类
 */
@Service
public class AdoptionPostServiceImpl implements AdoptionPostService {
    
    @Autowired
    private AdoptionPostMapper adoptionPostMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户端分页查询领养帖子列表（只显示"寻找中"的帖子，包含用户信息）
     */
    @Override
    public IPage<Map<String, Object>> userQueryAdoptionPosts(int page, int size, String petType, 
                                                               String petGender, String breed, String publisher, Long currentUserId) {
        QueryWrapper<AdoptionPost> queryWrapper = new QueryWrapper<>();
        
        // 只查询"寻找中"的帖子
        queryWrapper.eq("status", "寻找中");
        
        // 按宠物类型筛选
        if (petType != null && !petType.isEmpty()) {
            queryWrapper.eq("pet_type", petType);
        }
        
        // 按宠物性别筛选
        if (petGender != null && !petGender.isEmpty()) {
            queryWrapper.eq("pet_gender", petGender);
        }
        
        // 按品种筛选
        if (breed != null && !breed.isEmpty()) {
            queryWrapper.like("breed", breed);
        }
        
        // 按发布者筛选
        if (publisher != null && !publisher.isEmpty() && currentUserId != null) {
            if ("my".equals(publisher)) {
                queryWrapper.eq("user_id", currentUserId);
            } else if ("other".equals(publisher)) {
                queryWrapper.ne("user_id", currentUserId);
            }
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<AdoptionPost> pageParam = new Page<>(page, size);
        IPage<AdoptionPost> pageResult = adoptionPostMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为包含用户信息的Map
        List<Map<String, Object>> recordsWithUser = pageResult.getRecords().stream().map(post -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("userId", post.getUserId());
            map.put("title", post.getTitle());
            map.put("description", post.getDescription());
            map.put("petType", post.getPetType());
            map.put("petGender", post.getPetGender());
            map.put("petName", post.getPetName());
            map.put("breed", post.getBreed());
            map.put("vaccineStatus", post.getVaccineStatus());
            map.put("photos", post.getPhotos());
            map.put("adoptionRequirement", post.getAdoptionRequirement());
            map.put("location", post.getLocation());
            map.put("ownerInfo", post.getOwnerInfo());
            map.put("status", post.getStatus());
            map.put("createTime", post.getCreateTime());
            
            // 查询用户信息
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                map.put("userNickname", user.getNickname());
                map.put("userAvatar", user.getAvatar());
            }
            
            return map;
        }).collect(Collectors.toList());
        
        // 创建新的分页对象并设置转换后的记录
        Page<Map<String, Object>> resultPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        resultPage.setRecords(recordsWithUser);
        
        return resultPage;
    }
    
    /**
     * 用户端获取领养帖子详情（包含用户信息）
     */
    @Override
    public Map<String, Object> userGetAdoptionPostDetail(Long id) {
        AdoptionPost post = adoptionPostMapper.selectById(id);
        if (post == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", post.getId());
        map.put("userId", post.getUserId());
        map.put("title", post.getTitle());
        map.put("description", post.getDescription());
        map.put("petType", post.getPetType());
        map.put("petGender", post.getPetGender());
        map.put("petName", post.getPetName());
        map.put("breed", post.getBreed());
        map.put("vaccineStatus", post.getVaccineStatus());
        map.put("photos", post.getPhotos());
        map.put("adoptionRequirement", post.getAdoptionRequirement());
        map.put("location", post.getLocation());
        map.put("ownerInfo", post.getOwnerInfo());
        map.put("status", post.getStatus());
        map.put("createTime", post.getCreateTime());
        
        // 查询用户信息
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            map.put("userNickname", user.getNickname());
            map.put("userAvatar", user.getAvatar());
            map.put("userPhone", user.getPhone());
        }
        
        return map;
    }
    
    /**
     * 用户端发布领养帖子
     */
    @Override
    public boolean userAddAdoptionPost(AdoptionPost adoptionPost) {
        return adoptionPostMapper.insert(adoptionPost) > 0;
    }
    
    /**
     * 用户端更新领养帖子（只能更新自己发布的）
     */
    @Override
    public boolean userUpdateAdoptionPost(AdoptionPost adoptionPost, Long currentUserId) {
        // 先查询帖子是否存在且属于当前用户
        AdoptionPost existingPost = adoptionPostMapper.selectById(adoptionPost.getId());
        if (existingPost == null || !existingPost.getUserId().equals(currentUserId)) {
            return false;
        }
        
        return adoptionPostMapper.updateById(adoptionPost) > 0;
    }
    
    /**
     * 用户端修改领养帖子状态（只能修改自己发布的）
     */
    @Override
    public boolean userUpdateAdoptionPostStatus(Long id, String status, Long currentUserId) {
        // 先查询帖子是否存在且属于当前用户
        AdoptionPost existingPost = adoptionPostMapper.selectById(id);
        if (existingPost == null || !existingPost.getUserId().equals(currentUserId)) {
            return false;
        }
        
        AdoptionPost updatePost = new AdoptionPost();
        updatePost.setId(id);
        updatePost.setStatus(status);
        
        return adoptionPostMapper.updateById(updatePost) > 0;
    }
    
    /**
     * 根据ID获取领养帖子
     */
    @Override
    public AdoptionPost getAdoptionPostById(Long id) {
        return adoptionPostMapper.selectById(id);
    }
    
    /**
     * 管理端分页查询领养帖子列表（包含用户信息，所有状态）
     */
    @Override
    public IPage<Map<String, Object>> adminQueryAdoptionPosts(int page, int size, String petType, 
                                                                String petGender, String breed, String status) {
        QueryWrapper<AdoptionPost> queryWrapper = new QueryWrapper<>();
        
        // 按宠物类型筛选
        if (petType != null && !petType.isEmpty()) {
            queryWrapper.eq("pet_type", petType);
        }
        
        // 按宠物性别筛选
        if (petGender != null && !petGender.isEmpty()) {
            queryWrapper.eq("pet_gender", petGender);
        }
        
        // 按品种筛选
        if (breed != null && !breed.isEmpty()) {
            queryWrapper.like("breed", breed);
        }
        
        // 按状态筛选
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<AdoptionPost> pageParam = new Page<>(page, size);
        IPage<AdoptionPost> pageResult = adoptionPostMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为包含用户信息的Map
        List<Map<String, Object>> recordsWithUser = pageResult.getRecords().stream().map(post -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("userId", post.getUserId());
            map.put("title", post.getTitle());
            map.put("description", post.getDescription());
            map.put("petType", post.getPetType());
            map.put("petGender", post.getPetGender());
            map.put("petName", post.getPetName());
            map.put("breed", post.getBreed());
            map.put("vaccineStatus", post.getVaccineStatus());
            map.put("photos", post.getPhotos());
            map.put("adoptionRequirement", post.getAdoptionRequirement());
            map.put("location", post.getLocation());
            map.put("ownerInfo", post.getOwnerInfo());
            map.put("status", post.getStatus());
            map.put("createTime", post.getCreateTime());
            
            // 查询用户信息
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                map.put("userNickname", user.getNickname());
                map.put("userAvatar", user.getAvatar());
                map.put("userPhone", user.getPhone());
            }
            
            return map;
        }).collect(Collectors.toList());
        
        // 创建新的分页对象并设置转换后的记录
        Page<Map<String, Object>> resultPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        resultPage.setRecords(recordsWithUser);
        
        return resultPage;
    }
    
    /**
     * 管理端获取领养帖子详情（包含用户信息）
     */
    @Override
    public Map<String, Object> adminGetAdoptionPostDetail(Long id) {
        AdoptionPost post = adoptionPostMapper.selectById(id);
        if (post == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", post.getId());
        map.put("userId", post.getUserId());
        map.put("title", post.getTitle());
        map.put("description", post.getDescription());
        map.put("petType", post.getPetType());
        map.put("petGender", post.getPetGender());
        map.put("petName", post.getPetName());
        map.put("breed", post.getBreed());
        map.put("vaccineStatus", post.getVaccineStatus());
        map.put("photos", post.getPhotos());
        map.put("adoptionRequirement", post.getAdoptionRequirement());
        map.put("location", post.getLocation());
        map.put("ownerInfo", post.getOwnerInfo());
        map.put("status", post.getStatus());
        map.put("createTime", post.getCreateTime());
        
        // 查询用户信息
        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            map.put("userNickname", user.getNickname());
            map.put("userAvatar", user.getAvatar());
            map.put("userPhone", user.getPhone());
            map.put("userEmail", user.getEmail());
        }
        
        return map;
    }
    
    /**
     * 管理端删除领养帖子
     */
    @Override
    public boolean adminDeleteAdoptionPost(Long id) {
        // 查询帖子是否存在
        AdoptionPost post = adoptionPostMapper.selectById(id);
        if (post == null) {
            return false;
        }
        
        return adoptionPostMapper.deleteById(id) > 0;
    }
    
    @Override
    public boolean adminUpdateAdoptionPost(AdoptionPost adoptionPost) {
        return adoptionPostMapper.updateById(adoptionPost) > 0;
    }
}

