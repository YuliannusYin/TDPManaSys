# 教师数字画像系统 (Teacher Digital Portrait System)

高校教师科研成果统一管理与可视化数字画像展示平台。通过多维度成果数据的雷达图与统计图表，直观呈现教师综合能力，服务于个人发展自评与学院统筹管理。

---

## 技术栈

| 层 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.7.18 |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 数据库 | MySQL | 8.0 (Docker) |
| API 文档 | Knife4j (Swagger) | 4.1.0 |
| 认证 | JWT (jjwt) | 0.9.1 |
| Excel | EasyExcel | 3.3.2 |
| 前端框架 | Vue 3 (Composition API) | 3.3.4 |
| UI 组件库 | Element Plus | 2.3.12 |
| 图表库 | ECharts | 5.5.0 |
| 构建工具 | Vite | 4.4.9 |
| 状态管理 | Pinia | 2.1.6 |
| 路由 | Vue Router | 4.2.4 |
| HTTP 客户端 | Axios | 1.5.0 |

---

## 快速启动

### 前置条件

- **Node.js** >= 16
- **Java** >= 17
- **Docker Desktop**（用于数据库）

### Docker 一键启动（推荐）

```bash
cd teacher-portrait

# 1. 启动 MySQL 容器（10 张表 + 初始数据自动创建）
docker compose up -d

# 2. 启动后端（端口 8080）
cd portrait-server
./mvnw.cmd spring-boot:run

# 3. 启动前端（端口 3000）
cd portrait-web
npm install
npm run dev
```

### 手动启动

```bash
# 1. 在本地 MySQL 中执行 SQL 脚本
mysql -u root -p < portrait-server/src/main/resources/db/migration/V1__init.sql

# 2. 修改 application.yml 数据库连接信息后启动
cd portrait-server && ./mvnw.cmd spring-boot:run
cd portrait-web && npm install && npm run dev
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
| API 文档 (Knife4j) | http://localhost:8080/doc.html |
| 数据库（Docker） | localhost:3308 |

---

## 功能模块

| 模块 | 功能 | 状态 |
|------|------|:--:|
| 登录认证 | JWT 登录 + Token 存储 + 路由守卫 + 权限拦截 | ✅ |
| 首页仪表盘 | 快捷入口引导 | ✅ |
| 纵向项目管理 | CRUD + 分页多条件筛选 + 编号唯一性 + 级别/状态彩色标签 | ✅ |
| 横向项目管理 | CRUD + 合同金额校验 (>0) + 状态标签 | ✅ |
| 专利管理 | CRUD + 专利转让(仅已授权) + 类型/状态标签 + 转让记录 | ✅ |
| 软著管理 | CRUD + 登记号唯一性校验 | ✅ |
| 论文管理 | CRUD + 收录标签关联(8种) + 多选筛选 + 8色标签 | ✅ |
| 竞赛指导管理 | CRUD + 获奖级别/等级双标签着色 | ✅ |
| 数字画像 | 雷达图 + 对比模式 + 趋势图 + 饼图 + 仪表盘卡片 | ✅ |
| 用户管理 | CRUD + 工号唯一性 + 密码重置 + 不可删除自己 (仅ADMIN) | ✅ |
| 权重配置 | 5维度滑块 + 总和100%校验 + 计分规则编辑 + 缓存刷新 (仅ADMIN) | ✅ |
| 数据导入导出 | 6模块模板下载 + 批量导入(逐行校验) + 多条件导出 | ✅ |

---

## 权限模型

| 角色 | 可查看 | 可操作 |
|------|--------|--------|
| **TEACHER（教师）** | 仅本人数据 + 本人画像 | 本人数据的增删改查 |
| **ADMIN（管理员）** | 全院数据 + 所有功能菜单 | 所有数据增删改查 + 用户管理 + 权重配置 + 导入导出 |

- 后端：JWT 拦截器 → `@RequireRole` AOP 注解 → Service 层 `buildQueryWrapper()` 权限过滤 + `getById/update/delete` 所有权校验
- 前端：路由守卫 `router.beforeEach()` + 路由 `meta.role` + 侧栏 `v-if="userStore.role === 'ADMIN'"` 菜单条件渲染

---

## 项目规模

| 指标 | 数量 |
|------|:--:|
| 后端 Java 源文件 | 81 |
| 前端 Vue/JS 文件 | 25 |
| REST API 接口 | 42 |
| 数据库表 | 10 |
| ECharts 图表组件 | 4 |
| 测试用例 (阶段六) | 58 — 100% 通过 |

---

## 开发阶段

| 阶段 | 内容 | 状态 |
|:--:|------|:--:|
| 一 | 项目初始化与基础架构 | ✅ |
| 二 | 成果管理模块 CRUD（6 模块） | ✅ |
| 三 | 数字画像与可视化 | ✅ |
| 四 | 系统管理与配置 | ✅ |
| 五 | Excel 导入导出 | ✅ |
| 六 | 全流程测试与优化 (58用例 0Bug) | ✅ |

---

## 已知问题与待优化项

| 优先级 | 问题 | 影响范围 |
|:--:|------|------|
| 🟡 高 | 横向项目/专利(申请号/授权号)/论文(DOI)/竞赛(证书编号)缺少唯一性校验 | 5 个模块 |
| 🟢 低 | Vertical/Horizontal/Software/Competition 写操作缺少 @Transactional | 4 个模块 |
| 🟢 低 | PaperService 索引标签逐条 insert，应改为批量插入 | 1 个模块 |
| 🟢 低 | 归一化缓存在数据变更后需手动调用 `clearMaxCache()`，尚未集成到 CRUD 操作中 | 1 个模块 |
| 🟢 低 | ECharts 打包 chunk 较大 (PortraitView 1.13MB)，可异步加载优化 | 1 个文件 |
| 🟢 低 | 默认 admin 密码使用 MD5 加密，可考虑升级为 BCrypt | 安全 |
