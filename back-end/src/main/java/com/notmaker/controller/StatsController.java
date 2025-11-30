package com.notmaker.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.notmaker.entity.*;
import com.notmaker.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 统计数据控制器
 * 提供系统各项数据的统计分析接口
 */
@RestController
@RequestMapping("/api/stats")
@CrossOrigin
public class StatsController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private InsuranceMapper insuranceMapper;

    @Autowired
    private InsuranceOrderMapper insuranceOrderMapper;

    @Autowired
    private InsuranceClaimMapper insuranceClaimMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AdoptionPostMapper adoptionPostMapper;

    @Autowired
    private BreedingPostMapper breedingPostMapper;

    @Autowired
    private FosterPostMapper fosterPostMapper;

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private ForumPostLikeMapper forumPostLikeMapper;

    @Autowired
    private ForumPostCommentMapper forumPostCommentMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    /**
     * 获取管理端数据概览统计
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();

        // 用户统计
        Map<String, Object> userStats = new HashMap<>();
        Integer totalUsers = userMapper.selectCount(new QueryWrapper<User>().eq("role", "普通用户"));
        Integer normalUsers = userMapper.selectCount(new QueryWrapper<User>().eq("role", "普通用户").eq("status", "正常"));
        Integer disabledUsers = userMapper.selectCount(new QueryWrapper<User>().eq("role", "普通用户").eq("status", "禁用"));
        userStats.put("total", totalUsers);
        userStats.put("normal", normalUsers);
        userStats.put("disabled", disabledUsers);
        result.put("userStats", userStats);

        // 商品统计
        Map<String, Object> productStats = new HashMap<>();
        Integer totalProducts = productMapper.selectCount(null);
        Integer onSaleProducts = productMapper.selectCount(new QueryWrapper<Product>().eq("status", "上架"));
        Integer offSaleProducts = productMapper.selectCount(new QueryWrapper<Product>().eq("status", "下架"));
        productStats.put("total", totalProducts);
        productStats.put("onSale", onSaleProducts);
        productStats.put("offSale", offSaleProducts);
        result.put("productStats", productStats);

        // 订单统计
        Map<String, Object> orderStats = new HashMap<>();
        Integer totalOrders = orderMapper.selectCount(null);
        Integer pendingOrders = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "待发货"));
        Integer shippedOrders = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "已发货"));
        Integer completedOrders = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "已收货"));
        
        // 计算订单总金额
        List<Order> allOrders = orderMapper.selectList(null);
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Order order : allOrders) {
            totalAmount = totalAmount.add(order.getTotalAmount());
        }
        
        orderStats.put("total", totalOrders);
        orderStats.put("pending", pendingOrders);
        orderStats.put("shipped", shippedOrders);
        orderStats.put("completed", completedOrders);
        orderStats.put("totalAmount", totalAmount);
        result.put("orderStats", orderStats);

        // 保险统计
        Map<String, Object> insuranceStats = new HashMap<>();
        Integer totalInsurances = insuranceMapper.selectCount(null);
        Integer totalInsuranceOrders = insuranceOrderMapper.selectCount(null);
        Integer activeInsuranceOrders = insuranceOrderMapper.selectCount(new QueryWrapper<InsuranceOrder>().eq("status", "生效中"));
        Integer expiredInsuranceOrders = insuranceOrderMapper.selectCount(new QueryWrapper<InsuranceOrder>().eq("status", "已过期"));
        
        Integer totalClaims = insuranceClaimMapper.selectCount(null);
        Integer pendingClaims = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "待审核"));
        Integer approvedClaims = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "审核通过"));
        Integer rejectedClaims = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "审核拒绝"));
        Integer paidClaims = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "已打款"));
        
        insuranceStats.put("totalInsurances", totalInsurances);
        insuranceStats.put("totalOrders", totalInsuranceOrders);
        insuranceStats.put("activeOrders", activeInsuranceOrders);
        insuranceStats.put("expiredOrders", expiredInsuranceOrders);
        insuranceStats.put("totalClaims", totalClaims);
        insuranceStats.put("pendingClaims", pendingClaims);
        insuranceStats.put("approvedClaims", approvedClaims);
        insuranceStats.put("rejectedClaims", rejectedClaims);
        insuranceStats.put("paidClaims", paidClaims);
        result.put("insuranceStats", insuranceStats);

        // 医院预约统计
        Map<String, Object> hospitalStats = new HashMap<>();
        Integer totalHospitals = hospitalMapper.selectCount(null);
        Integer totalAppointments = appointmentMapper.selectCount(null);
        Integer pendingAppointments = appointmentMapper.selectCount(new QueryWrapper<Appointment>().eq("status", "待赴约"));
        Integer arrivedAppointments = appointmentMapper.selectCount(new QueryWrapper<Appointment>().eq("status", "已到店"));
        Integer completedAppointments = appointmentMapper.selectCount(new QueryWrapper<Appointment>().eq("status", "已完成"));
        
        hospitalStats.put("totalHospitals", totalHospitals);
        hospitalStats.put("totalAppointments", totalAppointments);
        hospitalStats.put("pending", pendingAppointments);
        hospitalStats.put("arrived", arrivedAppointments);
        hospitalStats.put("completed", completedAppointments);
        result.put("hospitalStats", hospitalStats);

        // 社区统计
        Map<String, Object> communityStats = new HashMap<>();
        Integer totalAdoptions = adoptionPostMapper.selectCount(null);
        Integer activeAdoptions = adoptionPostMapper.selectCount(new QueryWrapper<AdoptionPost>().eq("status", "寻找中"));
        Integer totalBreedings = breedingPostMapper.selectCount(null);
        Integer activeBreedings = breedingPostMapper.selectCount(new QueryWrapper<BreedingPost>().eq("status", "寻找中"));
        Integer totalFosters = fosterPostMapper.selectCount(null);
        Integer activeFosters = fosterPostMapper.selectCount(new QueryWrapper<FosterPost>().eq("status", "寻找中"));
        
        communityStats.put("totalAdoptions", totalAdoptions);
        communityStats.put("activeAdoptions", activeAdoptions);
        communityStats.put("totalBreedings", totalBreedings);
        communityStats.put("activeBreedings", activeBreedings);
        communityStats.put("totalFosters", totalFosters);
        communityStats.put("activeFosters", activeFosters);
        result.put("communityStats", communityStats);

        // 论坛统计
        Map<String, Object> forumStats = new HashMap<>();
        Integer totalPosts = forumPostMapper.selectCount(null);
        Integer totalLikes = forumPostLikeMapper.selectCount(null);
        Integer totalComments = forumPostCommentMapper.selectCount(null);
        
        forumStats.put("totalPosts", totalPosts);
        forumStats.put("totalLikes", totalLikes);
        forumStats.put("totalComments", totalComments);
        result.put("forumStats", forumStats);

        // 评价统计
        Integer totalReviews = reviewMapper.selectCount(null);
        result.put("totalReviews", totalReviews);

        return result;
    }

    /**
     * 获取商品分类分布数据
     */
    @GetMapping("/category-distribution")
    public List<Map<String, Object>> getCategoryDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        List<Category> categories = categoryMapper.selectList(null);
        for (Category category : categories) {
            Integer count = productMapper.selectCount(new QueryWrapper<Product>().eq("category_id", category.getId()));
            if (count > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", category.getName());
                item.put("value", count);
                result.add(item);
            }
        }
        
        return result;
    }

    /**
     * 获取订单状态分布数据
     */
    @GetMapping("/order-status-distribution")
    public List<Map<String, Object>> getOrderStatusDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Integer pending = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "待发货"));
        Integer shipped = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "已发货"));
        Integer completed = orderMapper.selectCount(new QueryWrapper<Order>().eq("status", "已收货"));
        
        if (pending > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "待发货");
            item.put("value", pending);
            result.add(item);
        }
        
        if (shipped > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "已发货");
            item.put("value", shipped);
            result.add(item);
        }
        
        if (completed > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "已收货");
            item.put("value", completed);
            result.add(item);
        }
        
        return result;
    }

    /**
     * 获取宠物类型分布数据（基于商品的宠物类型）
     */
    @GetMapping("/pet-type-distribution")
    public List<Map<String, Object>> getPetTypeDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Integer dogProducts = productMapper.selectCount(new QueryWrapper<Product>().eq("pet_type", "狗"));
        Integer catProducts = productMapper.selectCount(new QueryWrapper<Product>().eq("pet_type", "猫"));
        
        if (dogProducts > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "狗");
            item.put("value", dogProducts);
            result.add(item);
        }
        
        if (catProducts > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "猫");
            item.put("value", catProducts);
            result.add(item);
        }
        
        return result;
    }

    /**
     * 获取社区帖子分布数据
     */
    @GetMapping("/community-post-distribution")
    public List<Map<String, Object>> getCommunityPostDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Integer adoptions = adoptionPostMapper.selectCount(null);
        Integer breedings = breedingPostMapper.selectCount(null);
        Integer fosters = fosterPostMapper.selectCount(null);
        Integer forums = forumPostMapper.selectCount(null);
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("name", "宠物领养");
        item1.put("value", adoptions);
        result.add(item1);
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("name", "寻找配种");
        item2.put("value", breedings);
        result.add(item2);
        
        Map<String, Object> item3 = new HashMap<>();
        item3.put("name", "寻找寄养");
        item3.put("value", fosters);
        result.add(item3);
        
        Map<String, Object> item4 = new HashMap<>();
        item4.put("name", "论坛帖子");
        item4.put("value", forums);
        result.add(item4);
        
        return result;
    }

    /**
     * 获取理赔申请状态分布数据
     */
    @GetMapping("/claim-status-distribution")
    public List<Map<String, Object>> getClaimStatusDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Integer pending = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "待审核"));
        Integer approved = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "审核通过"));
        Integer rejected = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "审核拒绝"));
        Integer paid = insuranceClaimMapper.selectCount(new QueryWrapper<InsuranceClaim>().eq("status", "已打款"));
        
        if (pending > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "待审核");
            item.put("value", pending);
            result.add(item);
        }
        
        if (approved > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "审核通过");
            item.put("value", approved);
            result.add(item);
        }
        
        if (rejected > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "审核拒绝");
            item.put("value", rejected);
            result.add(item);
        }
        
        if (paid > 0) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", "已打款");
            item.put("value", paid);
            result.add(item);
        }
        
        return result;
    }
}

