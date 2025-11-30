package com.notmaker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@TableName("cart")
public class Cart {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 商品名称（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 商品图片（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String productImages;

    /**
     * 商品分类名称（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 商品价格（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private BigDecimal productPrice;

    /**
     * 商品品牌（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String productBrand;

    /**
     * 商品状态（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String productStatus;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", createTime=" + createTime +
                ", productName='" + productName + '\'' +
                ", productImages='" + productImages + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productPrice=" + productPrice +
                ", productBrand='" + productBrand + '\'' +
                ", productStatus='" + productStatus + '\'' +
                '}';
    }
}

