# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

宠物商城宠物之家系统 - 前后端分离的宠物服务平台，包含Web端（用户+管理员）和微信小程序端。

## 常用命令

### 后端
```bash
cd back-end
mvn clean compile          # 编译检查
mvn spring-boot:run        # 启动后端（端口18007）
```

### 前端
```bash
cd front-end
npm install                # 安装依赖
npm run serve              # 启动开发服务器（端口8080）
npm run build              # 构建生产版本
```

### 数据库
```bash
# 执行 ddl.sql 创建数据库和表结构
mysql -u pet_home -p < ddl.sql
```

## 技术栈

**后端**: Spring Boot 2.3.5 + MyBatis-Plus 3.3.1 + MySQL 8.0 + JWT + WebSocket
**前端**: Vue 2.6.11 + Vue Router + Element UI 2.15.13 + Axios + ECharts
**小程序**: 微信小程序

## 架构结构

```
back-end/src/main/java/com/notmaker/
├── controller/     # REST API控制器（20+个）
├── service/        # 服务层实现
├── mapper/         # MyBatis XML映射文件
├── entity/         # 实体类（20+个）
├── config/         # 配置类（CorsConfig、MybatisPlusConfig等）
└── util/           # 工具类（CacheManager内存缓存）

front-end/src/
├── api/            # 接口封装模块（user.js、product.js、order.js等）
├── router/         # Vue Router配置
├── views/          # 页面组件
├── components/     # 公共组件
└── utils/          # 工具函数（request.js、cacheHelper.js等）
```

## 关键配置

| 配置项 | 值 |
|--------|-----|
| 后端端口 | 18007 |
| 前端端口 | 8080 |
| 数据库 | pet_home |
| 文件上传目录 | D:\uploads\ |
| API基础URL | http://localhost:18007 |

## 认证与权限

- 用户名密码明文存储（`ddl.sql`中密码为`123456`）
- 登录后返回JWT token（保存在`localStorage`的`token`key）
- 路由使用`meta.role`控制权限（`普通用户`或`管理员`）
- 管理员默认账号: `admin` / `123456`
- 用户默认账号: `user001` / `123456`

## 代码规范

**后端**:
- 使用MyBatis-plus条件构造器（QueryWrapper/LambdaQueryWrapper）
- 禁止在for循环中执行SQL
- 实体类手写getter/setter（禁用Lombok）
- 所有表必须有`id`（bigint自增）和`create_time`（datetime）
- 状态字段使用varchar中文（如`正常`、`上架`）

**前端**:
- 使用Element UI组件库
- 登录密码明文对比（数据库存储明文）
- API接口封装在`src/api/`目录
- 文件上传保存完整URL路径到数据库

## 数据库

- 主数据库脚本: `ddl.sql`（根目录）
- 包含完整建表语句和示例数据
- 使用`DROP IF EXISTS`确保幂等性
- 所有表使用下划线命名法
