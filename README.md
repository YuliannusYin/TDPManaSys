# 教师数字画像系统 (Teacher Digital Portrait System)

高校教师科研成果统一管理与可视化数字画像展示平台。通过多维度成果数据的雷达图与统计图表，直观呈现教师综合能力，服务于个人发展自评与学院统筹管理。

---

## 技术栈

| 层 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.7.18 |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 数据库 | MySQL | 8.0 |
| API 文档 | Knife4j (Swagger) | 4.1.0 |
| 认证 | JWT (jjwt) | 0.9.1 |
| 前端框架 | Vue 3 | 3.3.4 |
| UI 组件库 | Element Plus | 2.3.12 |
| 构建工具 | Vite | 4.4.9 |
| 状态管理 | Pinia | 2.1.6 |
| 路由 | Vue Router | 4.2.4 |
| HTTP 客户端 | Axios | 1.5.0 |
| 数据库部署 | Docker Compose | MySQL 8.0 容器 |

---

## 项目目录结构

```
teacher-portrait/
├── docker-compose.yml                 # Docker MySQL 容器配置
├── portrait-server/                   # 后端项目 (Spring Boot)
│   ├── pom.xml                        # Maven 配置
│   ├── mvnw.cmd                       # Maven Wrapper
│   └── src/main/
│       ├── java/com/portrait/
│       │   ├── PortraitApplication.java    # 启动类
│       │   ├── config/                     # 配置（CORS/MyBatis-Plus/WebMVC/自动填充）
│       │   ├── controller/                 # 控制器（Auth + 6 个业务模块）
│       │   ├── service/                    # 业务逻辑层
│       │   ├── mapper/                     # MyBatis-Plus Mapper（10 表）
│       │   ├── entity/                     # 实体类（10 表）
│       │   ├── dto/                        # 请求 DTO
│       │   ├── vo/                         # 响应 VO
│       │   ├── common/                     # Result/PageResult/BusinessException
│       │   ├── interceptor/                # JWT 拦截器
│       │   ├── annotation/                 # RequireRole 权限注解
│       │   ├── aspect/                     # RoleAspect AOP 切面
│       │   └── util/                       # JwtUtil 工具类
│       └── resources/
│           ├── application.yml             # 应用配置
│           └── db/migration/
│               └── V1__init.sql            # 数据库初始化脚本（10 表 + 初始数据）
│
├── portrait-web/                      # 前端项目 (Vue 3 + Vite)
│   ├── package.json                   # 依赖配置
│   ├── vite.config.js                 # Vite 配置（/api 代理到 localhost:8080）
│   └── src/
│       ├── main.js                    # 入口（注册 Pinia/Router/ElementPlus）
│       ├── App.vue                    # 根组件
│       ├── router/index.js            # 路由配置 + 守卫
│       ├── store/user.js              # Pinia 用户状态
│       ├── api/                       # API 请求模块（auth + 6 业务模块）
│       ├── utils/token.js             # Token 持久化工具
│       └── views/
│           ├── login/LoginView.vue    # 登录页
│           ├── Layout.vue             # 主布局（侧栏菜单 + 顶栏）
│           ├── dashboard/             # 仪表盘首页
│           ├── project/               # 纵向/横向项目（列表 + 表单弹窗）
│           ├── patent/                # 专利管理（列表 + 表单 + 转让弹窗）
│           ├── software/              # 软著管理
│           ├── paper/                 # 论文管理（含收录标签多选）
│           ├── competition/           # 竞赛指导管理
│           ├── portrait/              # 数字画像（阶段三实现）
│           └── admin/                 # 系统管理（阶段四实现）
│
└── docs/                              # 项目文档
    ├── 项目计划书.md
    ├── 项目需求文档.md
    └── 数据库设计文档.md
```

---

## 快速启动

### 前置条件

- **Node.js** >= 16
- **Java** >= 17
- **Docker Desktop**（用于数据库）

### 方式一：Docker 一键启动（推荐）

```bash
# 1. 克隆项目后进入根目录
cd teacher-portrait

# 2. 启动 MySQL 容器（10 张核心表 + 初始数据自动创建）
docker compose up -d

# 3. 启动后端（端口 8080）
cd portrait-server
.\mvnw.cmd spring-boot:run
# 或者：java -jar target/portrait-server-1.0.0.jar

# 4. 启动前端（端口 3000，自动代理 /api 到后端）
cd ../portrait-web
npm install
npm run dev
```

### 方式二：手动启动

```bash
# 1. 在本地 MySQL 中执行 SQL 脚本
mysql -u root -p < portrait-server/src/main/resources/db/migration/V1__init.sql

# 2. 修改 portrait-server/src/main/resources/application.yml 中的数据库连接信息
#    url: jdbc:mysql://localhost:3308/teacher_portrait?...
#    username: root
#    password: root

# 3. 启动后端
cd portrait-server
.\mvnw.cmd spring-boot:run

# 4. 启动前端
cd portrait-web
npm install
npm run dev
```

### 默认测试账号

| 角色 | 工号 | 密码 |
|------|------|------|
| 管理员 | admin | 123456 |
| 教师 | T001 | 123456 |
| 教师 | T002 | 123456 |

### 访问地址

| 服务 | 地址 |
|------|------|
| 前端页面 | http://localhost:3000 |
| Swagger 文档 | http://localhost:8080/doc.html |
| 数据库（Docker） | localhost:3308 |

---

## 功能模块

| 模块 | 功能 | 状态 |
|------|------|:----:|
| 登录认证 | JWT 登录 + Token 存储 + 路由守卫 + 权限拦截 | ✅ |
| 首页仪表盘 | 数据汇总卡片 + 快速入口 | ✅ |
| 纵向项目管理 | 完整 CRUD + 分页多条件筛选 + 项目编号唯一性校验 | ✅ |
| 横向项目管理 | 完整 CRUD + 合同金额校验 (>0) | ✅ |
| 专利管理 | 完整 CRUD + 专利转让 + 状态/类型颜色标签 | ✅ |
| 软著管理 | 完整 CRUD + 登记号唯一性校验 | ✅ |
| 论文管理 | 完整 CRUD + 收录标签关联 + 多选筛选 | ✅ |
| 竞赛指导管理 | 完整 CRUD + 获奖级别/等级颜色标签 | ✅ |
| 数字画像 | 雷达图 + 对比 + 趋势 + 仪表盘（待开发） | ⬜ |
| 系统管理 | 用户管理 + 权重配置 + 导入导出（待开发） | ⬜ |

---

## API 接口概览

| 模块 | 接口路径 | 方法 |
|------|----------|------|
| 认证 | `/api/auth/login` | POST |
| 纵向项目 | `/api/vertical-projects` | GET / POST / PUT `/{id}` / DELETE `/{id}` |
| 横向项目 | `/api/horizontal-projects` | GET / POST / PUT `/{id}` / DELETE `/{id}` |
| 专利 | `/api/patents` | GET / POST / PUT `/{id}` / DELETE `/{id}` |
| 专利转让 | `/api/patents/{id}/transfer` | POST |
| 专利转让记录 | `/api/patents/{id}/transfers` | GET |
| 软著 | `/api/software-copyrights` | GET / POST / PUT `/{id}` / DELETE `/{id}` |
| 论文 | `/api/papers` | GET / POST / PUT `/{id}` / DELETE `/{id}` |
| 竞赛 | `/api/competitions` | GET / POST / PUT `/{id}` / DELETE `/{id}` |

---

## 权限模型

| 角色 | 可查看 | 可操作 |
|------|--------|--------|
| **TEACHER（教师）** | 仅本人数据 | 仅本人数据的增删改查 |
| **ADMIN（管理员）** | 全院数据 | 所有数据的增删改查 + 系统管理 |

- 后端：JWT 拦截器解析 Token → `@RequireRole` AOP 注解 + Service 层 `buildQueryWrapper()` 权限过滤
- 前端：路由守卫 `router.beforeEach()` + Layout 侧栏 `v-if="role === 'ADMIN'"` 菜单条件渲染

---

## 开发计划

| 阶段 | 内容 | 状态 |
|:----:|------|:----:|
| 阶段一 | 项目初始化与基础架构 | ✅ |
| 阶段二 | 成果管理模块 CRUD（6 模块） | ✅ |
| 阶段三 | 数字画像与可视化 | ⬜ |
| 阶段四 | 系统管理与配置 | ⬜ |
| 阶段五 | Excel 导入导出 | ⬜ |
| 阶段六 | 测试与优化 | ⬜ |
