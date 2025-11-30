package com.notmaker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notmaker.entity.BreedingPost;
import com.notmaker.entity.User;
import com.notmaker.mapper.BreedingPostMapper;
import com.notmaker.mapper.UserMapper;
import com.notmaker.service.BreedingPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 寻找配种帖子Service实现类
 */
@Service
public class BreedingPostServiceImpl implements BreedingPostService {
    
    @Autowired
    private BreedingPostMapper breedingPostMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户端分页查询配种帖子列表（只显示"寻找中"的帖子，包含用户信息）
     * @param page 当前页
     * @param size 每页大小
     * @param petType 宠物类型
     * @param petGender 宠物性别
     * @param breed 品种
     * @param publisher 发布者（my：我发布的，other：他人发布的）
     * @param currentUserId 当前用户ID
     * @return 分页结果（包含用户信息的Map列表）
     */
    @Override
    public IPage<Map<String, Object>> userQueryBreedingPosts(int page, int size, String petType, 
                                                               String petGender, String breed, String publisher, Long currentUserId) {
        // 创建查询条件
        QueryWrapper<BreedingPost> queryWrapper = new QueryWrapper<>();
        
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
                // 我发布的
                queryWrapper.eq("user_id", currentUserId);
            } else if ("other".equals(publisher)) {
                // 他人发布的
                queryWrapper.ne("user_id", currentUserId);
            }
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<BreedingPost> pageParam = new Page<>(page, size);
        IPage<BreedingPost> pageResult = breedingPostMapper.selectPage(pageParam, queryWrapper);
        
        // 将BreedingPost转换为包含用户信息的Map
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
            map.put("breedingRequirement", post.getBreedingRequirement());
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
     * 用户端获取配种帖子详情（包含用户信息）
     * @param id 帖子ID
     * @return 帖子详情Map（包含用户信息）
     */
    @Override
    public Map<String, Object> userGetBreedingPostDetail(Long id) {
        BreedingPost post = breedingPostMapper.selectById(id);
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
        map.put("breedingRequirement", post.getBreedingRequirement());
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
     * 用户端发布配种帖子
     * @param breedingPost 帖子对象
     * @return 发布结果
     */
    @Override
    public boolean userAddBreedingPost(BreedingPost breedingPost) {
        return breedingPostMapper.insert(breedingPost) > 0;
    }
    
    /**
     * 用户端更新配种帖子（只能更新自己发布的）
     * @param breedingPost 帖子对象
     * @param currentUserId 当前用户ID
     * @return 更新结果
     */
    @Override
    public boolean userUpdateBreedingPost(BreedingPost breedingPost, Long currentUserId) {
        // 先查询帖子是否存在且属于当前用户
        BreedingPost existingPost = breedingPostMapper.selectById(breedingPost.getId());
        if (existingPost == null || !existingPost.getUserId().equals(currentUserId)) {
            return false;
        }
        
        return breedingPostMapper.updateById(breedingPost) > 0;
    }
    
    /**
     * 用户端修改配种帖子状态（只能修改自己发布的）
     * @param id 帖子ID
     * @param status 状态：寻找中、已结束
     * @param currentUserId 当前用户ID
     * @return 修改结果
     */
    @Override
    public boolean userUpdateBreedingPostStatus(Long id, String status, Long currentUserId) {
        // 先查询帖子是否存在且属于当前用户
        BreedingPost existingPost = breedingPostMapper.selectById(id);
        if (existingPost == null || !existingPost.getUserId().equals(currentUserId)) {
            return false;
        }
        
        BreedingPost updatePost = new BreedingPost();
        updatePost.setId(id);
        updatePost.setStatus(status);
        
        return breedingPostMapper.updateById(updatePost) > 0;
    }
    
    /**
     * 根据ID获取配种帖子
     * @param id 帖子ID
     * @return 帖子对象
     */
    @Override
    public BreedingPost getBreedingPostById(Long id) {
        return breedingPostMapper.selectById(id);
    }
    
    /**
     * 管理端分页查询配种帖子列表（包含用户信息，所有状态）
     * @param page 当前页
     * @param size 每页大小
     * @param petType 宠物类型
     * @param petGender 宠物性别
     * @param breed 品种
     * @param status 状态
     * @return 分页结果（包含用户信息的Map列表）
     */
    @Override
    public IPage<Map<String, Object>> adminQueryBreedingPosts(int page, int size, String petType, 
                                                                String petGender, String breed, String status) {
        // 创建查询条件
        QueryWrapper<BreedingPost> queryWrapper = new QueryWrapper<>();
        
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
        Page<BreedingPost> pageParam = new Page<>(page, size);
        IPage<BreedingPost> pageResult = breedingPostMapper.selectPage(pageParam, queryWrapper);
        
        // 将BreedingPost转换为包含用户信息的Map
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
            map.put("breedingRequirement", post.getBreedingRequirement());
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
     * 管理端获取配种帖子详情（包含用户信息）
     * @param id 帖子ID
     * @return 帖子详情Map（包含用户信息）
     */
    @Override
    public Map<String, Object> adminGetBreedingPostDetail(Long id) {
        BreedingPost post = breedingPostMapper.selectById(id);
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
        map.put("breedingRequirement", post.getBreedingRequirement());
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
     * 管理端删除配种帖子
     * @param id 帖子ID
     * @return 删除结果
     */
    @Override
    public boolean adminDeleteBreedingPost(Long id) {
        // 查询帖子是否存在
        BreedingPost post = breedingPostMapper.selectById(id);
        if (post == null) {
            return false;
        }
        
        return breedingPostMapper.deleteById(id) > 0;
    }
    
    /**
     * 管理端更新配种帖子
     * @param breedingPost 帖子对象
     * @return 更新结果
     */
    @Override
    public boolean adminUpdateBreedingPost(BreedingPost breedingPost) {
        // 查询帖子是否存在
        BreedingPost existingPost = breedingPostMapper.selectById(breedingPost.getId());
        if (existingPost == null) {
            return false;
        }
        
        return breedingPostMapper.updateById(breedingPost) > 0;
    }
}

