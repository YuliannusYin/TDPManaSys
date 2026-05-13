# 开发日志 (Development Log)

教师数字画像系统 开发全过程记录。

***

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

- ✅ 编写 DDL 脚本 V1\_\_init.sql（10 张核心表 + 初始数据）
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
- ✅ 前端：LoginView\.vue 登录页（表单校验 + Token 存储 + 登录后重定向）
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

***

## 2026-05-12 — 阶段二：成果管理 CRUD（6 模块）

### 纵向项目管理 (VerticalProject)

- ✅ 后端：DTO × 2（新增/编辑 + 查询）+ VO（含教师姓名/学院关联）
- ✅ 后端：Service — 完整 CRUD + 分页多条件查询（名称/编号/级别/状态/角色/年份区间/经费区间）
- ✅ 后端：Service — 权限过滤（TEACHER 只看本人，在 buildQueryWrapper 中处理）
- ✅ 后端：Service — 项目编号唯一性校验（新增查重，编辑排除自身）
- ✅ 后端：Controller — 5 个 REST 接口（GET/POST/PUT/DELETE）
- ✅ 前端：API 封装（verticalProject.js）
- ✅ 前端：VerticalProjectView\.vue（搜索栏 + 表格 + 分页 + 级别/状态标签着色）
- ✅ 前端：ProjectFormDialog.vue（新增/编辑弹窗 + 必填校验）
- ⚠️ 遇到问题：空数据库查询 500 错误 — `selectBatchIds(emptyList)` 生成非法 SQL
- ✅ 修复：空列表跳过 selectBatchIds，用 Collections.emptyMap() 兜底

### 横向项目管理 (HorizontalProject)

- ✅ 后端：DTO × 2 + VO + Service + Controller（参照纵向项目模板）
- ✅ 后端：Service — 合同金额 `@DecimalMin("0.01")` 校验（DTO 层 + Service 层双重）
- ✅ 前端：HorizontalProjectView\.vue + HorizontalProjectFormDialog.vue

### 专利管理 (Patent)

- ✅ 后端：DTO × 3（新增/编辑 + 查询 + 转让）+ VO × 2（专利 + 转让记录）
- ✅ 后端：Service — CRUD + 专利转让功能（仅已授权可转让）
- ✅ 后端：Service — 转让 `@Transactional` 保证原子性
- ✅ 后端：Controller — 7 个接口（含 `POST /{id}/transfer` + `GET /{id}/transfers`）
- ✅ 前端：PatentView\.vue（状态/类型颜色标签 + 转让按钮仅已授权可见）
- ✅ 前端：PatentFormDialog.vue + TransferDialog.vue

### 软著管理 (SoftwareCopyright)

- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 后端：Service — `checkRegistrationNoUnique()` 登记号唯一性校验（最规范实现，推荐模板）
- ✅ 前端：SoftwareView\.vue + SoftwareFormDialog.vue

### 论文管理 (Paper)

- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 后端：Service — 收录标签关联（PaperIndex 子表）+ 多选筛选
- ✅ 后端：Service — `@Transactional` 全量事务（新增/编辑/删除均含子表操作）
- ✅ 后端：Service — 收录标签筛选：逗号分隔 → PaperIndex 表 IN 查询 → 反查 Paper
- ✅ 前端：PaperView\.vue（收录标签 8 色区分）+ PaperFormDialog.vue（多选下拉）
- ⚠️ 编译错误：lambda 中使用重新赋值的 `indexMap` → 改为三目运算符

### 竞赛指导管理 (Competition)

- ✅ 后端：DTO × 2 + VO + Service + Controller
- ✅ 前端：CompetitionView\.vue（获奖级别/获奖等级双标签着色）
- ✅ 前端：CompetitionFormDialog.vue
- ✅ 阶段二全部 6 模块完成：69 源文件 + 21 前端文件，32+2 个 API 接口

***

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

***

## 2026-05-13 — 阶段三：数字画像与可视化

### 后端评分计算引擎 (ScoreCalculationService)

- ✅ 创建 ScoreCalculationService（75 行 × 5 维度计分引擎）
- ✅ `calcProjectScore()` — 纵向项目四级阶梯（主持 20/15/10/5 + 参与折半）+ 横向项目按合同金额每 10 万 +2 分
- ✅ `calcPatentScore()` — 发明/实用新型/外观三档（已授权 20/10/5 + 申请中减半）+ 转让额外 +5/项
- ✅ `calcSoftwareScore()` — 已登记 +10/项
- ✅ `calcPaperScore()` — A/B/C/D 四档（25/15/10/5）+ 非第一作者权重 0.5 + 论文等级从收录标签映射（SCI/SSCI→A 类, EI/CSCD→B 类, CSSCI/北大核心→C 类, 其他→D 类）
- ✅ `calcCompetitionScore()` — 国/省/校三级五档梯次（国特30→校三3）+ 非第一指导×0.7
- ✅ `calculateRadar()` — 返回 5 维原始分 + 归一化分 + 教师基本信息
- ✅ `calculateDashboard()` — 返回经费总额、A/B 类论文数、已授权专利数、软著数、竞赛获奖数、原始分、归一化分
- ✅ `calculateTrend()` — 近 5 年逐年统计（项目数+经费、专利数、软著数、论文数、竞赛数）
- ✅ `calculateDistribution()` — 四类饼图数据（项目级别分布、专利类型分布、论文等级分布、竞赛级别分布）
- ✅ `compareRadars()` — 多教师对比雷达图（取全局最高分归一化）
- ⚠️ 遇到问题：`int` 接收 `selectCount()` 返回值 → 编译错误 → 改为 `long`
- ⚠️ 遇到问题：论文等级映射需取最高收录优先级（如多收录取最高级）→ `loadPaperClasses()` 按优先级合并

### 后端 PortraitController

- ✅ 创建 PortraitController — 6 个接口
- ✅ GET `/api/portrait/{userId}/radar` — 权限：TEACHER 仅本人
- ✅ GET `/api/portrait/{userId}/dashboard` — 权限：TEACHER 仅本人
- ✅ GET `/api/portrait/{userId}/trend` — 权限：TEACHER 仅本人
- ✅ GET `/api/portrait/{userId}/distribution` — 权限：TEACHER 仅本人
- ✅ GET `/api/portrait/compare?userIds=1,2` — **仅 ADMIN** 可调用，参数校验 2-5 人
- ✅ GET `/api/portrait/teachers` — 返回全院教师列表（下拉选择器数据源）
- ⚠️ 遇到问题：`checkAccess()` 中默认抛 `BusinessException("无权查看他人数据")` code=500 → 改为 code=403

### 前端数字画像页面

- ✅ 安装 ECharts 5（`npm install echarts --save`）
- ✅ 创建 portrait.js API 模块（6 个接口封装）
- ✅ 创建 PortraitView\.vue — 画像主页面
  - 顶部 6 张概览卡片（经费、A/B 类论文、专利、软著、竞赛、综合均分），综合均分 <30 红 / 30-60 黄 / ≥60 绿
  - 管理员：下拉选择器切换全院教师；普通教师：默认本人
  - 支持路由参数 `/portrait/:userId?` 直接定位
- ✅ 创建 RadarChart.vue — ECharts 雷达图
  - 5 维指标：科研项目/专利成果/软件著作/学术论文/竞赛指导
  - 原始得分 / 归一化得分开关切换
  - 管理员对比模式：多选 2-5 人叠加展示（不同颜色线条）
- ✅ 创建 TrendChart.vue — ECharts 柱状图 + 折线图
  - 5 模块 Tab 切换（项目/专利/软著/论文/竞赛）
  - 项目模块额外渲染经费折线（双 Y 轴）
  - X 轴：年份（近 5 年），Y 轴：数量
- ✅ 创建 DistributionChart.vue — ECharts 环形饼图
  - 4 类分布切换：项目级别 / 专利类型 / 论文类别 / 竞赛级别
  - 数据通过 `/api/portrait/{userId}/distribution` 动态获取

### 测试验证

- ✅ 插入 T001(张教授) 和 T002(李教授) 差异化测试数据（各模块数量不同）
- ✅ Radar 手动计分验证：T001 科研项目=45.00(国主20+省主15+市参与5+校主5)、专利=25.00、软著=20.00、论文=37.50(SCI一作25+EI二作7.5+普通5)、竞赛=35.00(国一25+省二10) — **全部命中**
- ✅ Dashboard 汇总验证：经费125万、A类1篇、B类1篇、已授权1、软著2、竞赛2 — **全部命中**
- ✅ Compare 对比验证：T001/T002 归一化合理（T001 软著低于 T002，对比归一化 66.7% / 100.0%）
- ✅ Trend 趋势验证：逐年数据（2022-2026）与测试数据完全匹配
- ✅ Distribution 分布验证：4 类饼图数据正确
- ✅ 归一化边界验证：所有归一化值均在 \[0, 100] 区间内
- ✅ 权限验证：T001 查自己 → 200；T001 查 T002 → 403；T001 调用 compare → 403
- ✅ 后端编译：75 源文件 BUILD SUCCESS
- ✅ 前端构建：2282 modules built（含 ECharts）

### Issue 发现与修复

- ❌ Issue1：distribution 接口缺失 `checkAccess()` → ✅ 添加权限校验
- ❌ Issue2：`normalizeScores()` 每次全表扫描全量计算 → ✅ 新增 `volatile cachedGlobalMaxes` + DCL 缓存 + `clearMaxCache()` 刷新接口
- ❌ Issue3：PortraitView 给 DistributionChart 传入空对象 `{}` → ✅ 移除冗余 prop（组件已自行调用 API）
- ❌ Issue4：RadarChart 获取对比数据后未 emit 到父组件 → ✅ 新增 `emit('update:compareData')` + 父组件 `onCompareData()` 更新状态

***

## 2026-05-13 — 阶段四：系统管理与配置

### 用户管理

- ✅ 后端：UserDTO + UserService + UserController（CRUD + 密码重置 + 业务规则）
- ✅ UserService — 工号唯一性校验（新增+编辑）、不能删除自己、密码 MD5 加密
- ✅ 前端：UserManageView\.vue（表格 + 搜索 + 新增/编辑/重置密码/删除弹窗）
- ✅ 角色标签着色：ADMIN(红) / TEACHER(默认)

### 评分权重配置

- ✅ 后端：ScoreConfigController（GET/PUT + 总和 100% 校验 + 清空画像缓存）
- ✅ 前端：ScoreConfigView\.vue（5 维滑块 + 实时求总和 + 计分规则 JSON 编辑）
- ✅ 保存后触发 `clearMaxCache()` 使画像重新计算

### 验证

- ✅ 用户 CRUD 全流程：新增→登录→重置密码→登录→删除（非自己）
- ✅ 工号重复 → 拒绝；管理员删除自己 → 拒绝
- ✅ 权重和 110% → 拒绝；和 100% → 保存成功
- ✅ 后端编译：79 文件 BUILD SUCCESS

***

## 2026-05-13 — 阶段五：Excel 导入导出

### 后端

- ✅ pom.xml 添加 EasyExcel 3.3.2 依赖
- ✅ 创建 ExcelController — 3 类接口
- ✅ GET `/api/excel/template/{module}` — 6 模块模板下载（含示例数据行）
- ✅ POST `/api/excel/import/{module}` — 逐行校验导入（必填/枚举/唯一性/金额）
- ✅ GET `/api/excel/export/{module}` — 多条件导出（userId + year 筛选）
- ✅ ImportResultDTO — 成功/跳过/失败计数 + 原因列表
- ✅ 模板字段有序对齐：work\_no 列支持指定教师

### 前端

- ✅ request.js 新增 blob 响应类型支持（导出文件下载）
- ✅ ImportExportView\.vue（6 模块 Tab + 上传 + 结果展示 + 导出筛选）
- ✅ 管理员可选教师和年份筛选导出，普通教师仅导出本人

### 验证

- ✅ 模板下载 3716 bytes
- ✅ 导出 vertical-project 4144 bytes + 年份筛选
- ✅ 后端编译：81 文件 BUILD SUCCESS

***

## 2026-05-13 — 阶段六：全流程测试与优化

### 测试执行

- ✅ 登录与权限（5 用例）
- ✅ 6 模块 CRUD 完整流程（16 用例）
- ✅ 权限隔离（4 用例）
- ✅ 数字画像（11 用例）
- ✅ 系统管理（9 用例）
- ✅ Excel 导入导出（3 用例）
- ✅ 边界测试（5 用例）
- ✅ 性能检查（5 用例 — 数据库索引/EXPLAIN/懒加载）

### 测试结果

- ✅ **58/58 用例全部通过，0 Bug 发现**
- ✅ 空用户画像 → 5 维 0 分不报错
- ✅ 归一化值全在 \[0, 100]
- ✅ 数据库 3 个索引全部生效
- ✅ 12 条路由全部懒加载
- ✅ ECharts 按需引入

### 收尾工作

- ✅ 更新 README.md（完整功能列表 + 技术栈 + 已知问题）
- ✅ 更新 DEVLOG.md（阶段四至六完整记录）
- ✅ 创建 docs/PROJECT.md（完整项目结构文档，供 AI 上下文理解）
- ✅ 确认 Knife4j API 文档可访问 (/doc.html, 200)
- ⚠️ Swagger UI (/swagger-ui.html) 返回 404 — 因为项目使用 Knife4j OpenAPI3 模式，非标准 Swagger
- ✅ 列出 7 条已知技术债务

### Issue 修复

- ✅ Issue1: ScoreConfigView `weight: Number(item.weight || item.weight)` 冗余 → 简化
- ✅ Issue2: ScoreConfigController `Long.valueOf(c.get("id").toString())` NPE 风险 → null 检查

***

## 当前项目状态

### 已完成

|  阶段  | 内容                         |  状态 |
| :--: | -------------------------- | :-: |
|  阶段一 | 项目初始化 + 登录认证 + 路由守卫 + 权限拦截 |  ✅  |
|  阶段二 | 6 个成果管理模块完整 CRUD           |  ✅  |
| 安全修复 | 3 个跨用户操作漏洞修复               |  ✅  |
|  阶段三 | 数字画像与可视化                   |  ✅  |
|  阶段四 | 系统管理与配置（用户管理/权重配置）         |  ✅  |
|  阶段五 | Excel 导入导出                 |  ✅  |
|  阶段六 | 全流程测试与优化（58 用例 100% 通过）    |  ✅  |

### 源码规模

| 指标           |     数量    |
| ------------ | :-------: |
| 后端 Java 源文件  |     81    |
| 前端 Vue/JS 文件 |     25    |
| REST API 接口  |     42    |
| 数据库表         |     10    |
| ECharts 图表组件 |     4     |
| 测试用例         | 58 — 全部通过 |

***

## 已知技术债务

|  优先级 | 问题                                                            | 影响范围  |
| :--: | ------------------------------------------------------------- | ----- |
| 🟡 高 | 横向项目/专利(申请号/授权号)/论文(DOI)/竞赛(证书编号)缺少唯一性校验                      | 5 个模块 |
| 🟢 低 | Vertical/Horizontal/Software/Competition 写操作缺少 @Transactional | 4 个模块 |
| 🟢 低 | PaperService 索引标签逐条 insert，应改为批量插入                            | 1 个模块 |
| 🟢 低 | 归一化缓存在数据变更后需手动调用 `clearMaxCache()`，尚未集成到 CRUD                 | 1 个模块 |
| 🟢 低 | ECharts 打包 chunk 较大 (PortraitView 1.13MB)，可异步加载优化             | 1 个文件 |
| 🟢 低 | Knife4j OpenAPI3 模式无 /swagger-ui.html，仅 /doc.html 可用          | 文档入口  |

