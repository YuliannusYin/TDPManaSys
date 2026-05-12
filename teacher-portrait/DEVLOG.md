# 开发日志 (Development Log)

教师数字画像系统 开发全过程记录。

---

## 2026-05-11 — 阶段一：项目初始化与基础架构

### 环境准备
- ✅ 创建 `teacher-portrait` 项目根目录及 `portrait-server` / `portrait-web` / `docs` 子目录
- ✅ 初始化 Spring Boot 2.7.18 后端项目（pom.xml, application.yml, 启动类）
- ✅ 初始化 Vue 3 + Vite 前端项目（package.json, vite.config.js, main.js, App.vue）
- ✅ 检测环境：Node.js v24、Java 21、Docker 29.4.1 均已就绪，Maven 未安装改用 Maven Wrapper

### 后端基础配置
- ✅ 配置 MyBatis-Plus 3.5.3.1（分页插件、自动填充 createTime/updateTime）
- ✅ 配置 Knife4j (Swagger) API 文档
- ✅ 配置 CORS 跨域过滤器（允许所有来源）
- ✅ 创建统一响应体 Result、分页 PageResult、业务异常 BusinessException、全局异常处理器
- ✅ 创建 JwtUtil 工具类（生成/解析 Token，24h 过期）
- ✅ 创建 JwtInterceptor 拦截器（校验 Bearer Token，解析 userId/workNo/role/name）
- ✅ 创建 @RequireRole 注解 + RoleAspect AOP 切面（角色权限校验）
- ✅ 注册拦截器到 `/api/**`，排除登录和文档接口

### 数据库
- ✅ 编写 DDL 脚本 V1__init.sql（10 张核心表 + 初始数据）
- ⚠️ 遇到问题：Docker MySQL ENUM 中文值在挂载初始化时编码损坏，数据库/表为 utf8mb4 但客户端连接为 latin1
- ✅ 修复方案 1：docker-compose.yml 添加 `--character-set-client-handshake=FALSE` + `--skip-character-set-client-handshake`
- ✅ 修复方案 2：SQL 文件首行添加 `SET NAMES utf8mb4;`
- ✅ 修复方案 3：application.yml JDBC URL 添加 `allowPublicKeyRetrieval=true&connectionCollation=utf8mb4_unicode_ci`
- ✅ 修复方案 4：Spring 配置 `server.servlet.encoding.force=true` 强制 UTF-8 响应
- ✅ 最终采用 `docker cp` + 容器内 `source` 方式执行 SQL，绕开挂载编码问题
- ✅ 10 张表全部创建成功，初始数据（3 用户 + 5 权重配置）正确入库
- ⚠️ 端口冲突：3306/3307 被其他 Docker 容器占用，最终使用 3308:3306 映射

### 登录与认证
- ✅ 后端：POST `/api/auth/login` — MD5 密码校验 + JWT Token 生成
- ✅ 前端：LoginView.vue 登录页（表单校验 + Token 存储 + 登录后重定向）
- ✅ 前端：axios 请求拦截器自动携带 Bearer Token
- ✅ 前端：axios 响应拦截器 401 自动跳转登录页
- ✅ 前端：Pinia userStore 状态管理（login/logout + 持久化）
- ✅ 后端：JWT 拦截器未登录返回 401 `{"code":401,"message":"未登录，请先登录"}`

### 路由与布局
- ✅ 前端：Vue Router 路由配置（13 条路由，Layout 嵌套路由）
- ✅ 前端：路由守卫 `beforeEach`（无 Token → 重定向登录；角色 meta 校验 → 无权限重定向首页）
- ✅ 前端：Layout.vue 主布局（可折叠侧栏 + 顶栏用户信息 + 退出登录）
- ✅ 前端：管理员菜单条件渲染 `v-if="userStore.role === 'ADMIN'"`
- ✅ 前端：8 个占位页面（后续阶段实现）

### 验证
- ✅ 前端构建：1681 modules built（npm run build）
- ✅ 后端编译：37 源文件 BUILD SUCCESS（mvn compile）
- ✅ API 测试：admin/T001 登录 → 200 + JWT Token；无 Token 访问 → 401 拦截
- ✅ 项目骨架可运行，登录流程走通

---

## 2026-05-12 — 阶段二：成果管理 CRUD（6 模块）

### 纵向项目管理 (VerticalProject)
- ✅ 后端：DTO × 2（新增/编辑 + 查询）+ VO（含教师姓名/学院关联）
- ✅ 后端：Service — 完整 CRUD + 分页多条件查询（名称/编号/级别/状态/角色/年份区间/经费区间）
- ✅ 后端：Service — 权限过滤（TEACHER 只看本人，在 buildQueryWrapper 中处理）
- ✅ 后端：Service — 项目编号唯一性校验（新增查重，编辑排除自身）
- ✅ 后端：Controller — 5 个 REST 接口（GET/POST/PUT/DELETE）
- ✅ 前端：API 封装（verticalProject.js）
- ✅ 前端：VerticalProjectView.vue（搜索栏 + 表格 + 分页 + 级别/状态标签着色）
- ✅ 前端：ProjectFormDialog.vue（新增/编辑弹窗 + 必填校验）
- ⚠️ 遇到问题：空数据库查询 500 错误 — `selectBatchIds(emptyList)` 生成非法 SQL
- ✅ 修复：空列表跳过 selectBatchIds，用 Collections.emptyMap() 兜底

### 横向项目管理 (HorizontalProject)
- ✅ 后端：DTO × 2 + VO + Service + Controller（参照纵向项目模板）
- ✅ 后端：Service — 合同金额 `@DecimalMin("0.01")` 校验（DTO 层 + Service 层双重）
- ✅ 前端：HorizontalProjectView.vue + HorizontalProjectFormDialog.vue

### 专利管理 (Patent)
- ✅ 后端：DTO × 3（新增/编辑 + 查询 + 转让）+ VO × 2（专利 + 转让记录）
- ✅ 后端：Service — CRUD + 专利转让功能（仅已授权可转让）
- ✅ 后端：Service — 转让 `@Transactional` 保证原子性
- ✅ 后端：Controller — 7 个接口（含 `POST /{id}/transfer` + `GET /{id}/transfers`）
- ✅ 前端：PatentView.vue（状态/类型颜色标签 + 转让按钮仅已授权可见）
- ✅ 前端：PatentFormDialog.vue + TransferDialog.vue

### 软著管理 (SoftwareCopyright)
- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 后端：Service — `checkRegistrationNoUnique()` 登记号唯一性校验（最规范实现，推荐模板）
- ✅ 前端：SoftwareView.vue + SoftwareFormDialog.vue

### 论文管理 (Paper)
- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 后端：Service — 收录标签关联（PaperIndex 子表）+ 多选筛选
- ✅ 后端：Service — `@Transactional` 全量事务（新增/编辑/删除均含子表操作）
- ✅ 后端：Service — 收录标签筛选：逗号分隔 → PaperIndex 表 IN 查询 → 反查 Paper
- ✅ 前端：PaperView.vue（收录标签 8 色区分）+ PaperFormDialog.vue（多选下拉）
- ⚠️ 编译错误：lambda 中使用重新赋值的 `indexMap` → 改为三目运算符

### 竞赛指导管理 (Competition)
- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 前端：CompetitionView.vue（获奖级别/获奖等级双标签着色）
- ✅ 前端：CompetitionFormDialog.vue
- ✅ 阶段二全部 6 模块完成：69 源文件 + 21 前端文件，32+2 个 API 接口

---

## 2026-05-12 — 阶段二审计与安全修复

### 审计发现问题
- ❌ **严重漏洞 1：update 强制覆盖 userId** — 所有 6 个 Service 的 update() 方法都执行了 `entity.setUserId(getCurrentUserId())`，使他人数据被变更为当前用户所有
- ❌ **严重漏洞 2：delete 无所有权校验** — 任何登录用户可删除他人记录
- ❌ **严重漏洞 3：getById 无权限过滤** — TEACHER 角色可通过 ID 查看任意教师数据详情
- ⚠️ 枚举值对齐检查：36 个枚举值 100% 匹配计划书 ✅
- ⚠️ 唯一性校验缺失：横向项目（无）、专利申请号/授权号、论文 DOI、竞赛证书编号（后续补充）
- ⚠️ 缺少 @Transactional：VerticalProject / HorizontalProject / SoftwareCopyright / Competition 写操作未加事务（低优先级）

### 修复内容
- ✅ 漏洞 1 修复：移除所有 update() 中的 `setUserId(getCurrentUserId())`，改为所有权校验 `if (!entity.getUserId().equals(getCurrentUserId())) throw BusinessException("无权修改他人数据")`
- ✅ 漏洞 2 修复：所有 delete() 方法增加所有权校验 `throw BusinessException("无权删除他人数据")`
- ✅ 漏洞 3 修复：所有 getById() 方法增加 TEACHER 跨用户检查 `throw BusinessException("无权查看他人数据")`
- ✅ 新增 `getCurrentUserRole()` 辅助方法（6 个 Service 统一）
- ✅ 修复后编译通过：69 文件 BUILD SUCCESS
- ✅ 代码审查确认：`setUserId(getCurrentUserId())` 仅保留在 create() 中（合法），update() 中全部移除

---

## 已知技术债务

| 优先级 | 问题 | 影响范围 |
|:--:|------|------|
| 🟡 高 | 横向项目/专利(申请号/授权号)/论文(DOI)/竞赛(证书编号)缺少唯一性校验 | 5 个模块 |
| 🟢 低 | Vertical/Horizontal/Software/Competition 写操作缺少 @Transactional | 4 个模块 |
| 🟢 低 | PaperService 索引标签逐条 insert，应改为批量插入 | 1 个模块 |
| 🟢 低 | PaperService indexTypes 类型不一致（DTO List vs QueryDTO String） | 1 个模块 |
| 🟢 低 | 用户管理页面仅占位（需阶段四实现） | 1 个页面 |
| 🟢 低 | 后端正则清理占位测试数据 | 所有模块 |
