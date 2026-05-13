# 教师数字画像系统 - 项目完整结构与功能说明

面向 AI 开发助手的项目快速理解文档。

---

## 一、项目结构

```
teacher-portrait/                         # 项目根目录
├── docker-compose.yml                    # Docker MySQL 8.0 容器 (端口 3308:3306)
├── README.md                             # 项目说明文档
├── DEVLOG.md                             # 全阶段开发日志
│
├── portrait-server/                      # 后端项目 (Spring Boot 2.7.18 + Maven)
│   ├── pom.xml                           # Maven 依赖 (MyBatis-Plus/Knife4j/JWT/EasyExcel)
│   ├── mvnw.cmd                          # Maven Wrapper (Windows)
│   └── src/main/
│       ├── java/com/portrait/
│       │   ├── PortraitApplication.java  # Spring Boot 启动类
│       │   ├── annotation/
│       │   │   └── RequireRole.java      # 角色权限注解 (@RequireRole("ADMIN"))
│       │   ├── aspect/
│       │   │   └── RoleAspect.java       # AOP 切面 (解析 @RequireRole)
│       │   ├── common/
│       │   │   ├── BusinessException.java # 业务异常 (code + message)
│       │   │   ├── GlobalExceptionHandler.java # 全局异常处理器 (@RestControllerAdvice)
│       │   │   ├── PageResult.java       # 分页响应 (total/page/size/records)
│       │   │   └── Result.java           # 统一响应体 (code/message/data)
│       │   ├── config/
│       │   │   ├── CorsConfig.java       # CORS 跨域过滤器
│       │   │   ├── MetaObjectHandlerConfig.java # MyBatis-Plus 自动填充 (createTime/updateTime)
│       │   │   ├── MybatisPlusConfig.java # MyBatis-Plus 分页插件
│       │   │   └── WebMvcConfig.java     # 拦截器注册 (JwtInterceptor 注册到 /api/**)
│       │   ├── controller/               # 控制器层 (共 11 个)
│       │   │   ├── AuthController.java   # POST /api/auth/login
│       │   │   ├── VerticalProjectController.java  # 纵向项目 CRUD (/api/vertical-projects)
│       │   │   ├── HorizontalProjectController.java # 横向项目 CRUD (/api/horizontal-projects)
│       │   │   ├── PatentController.java # 专利 CRUD + 转让 (/api/patents)
│       │   │   ├── SoftwareCopyrightController.java # 软著 CRUD (/api/software-copyrights)
│       │   │   ├── PaperController.java  # 论文 CRUD (/api/papers)
│       │   │   ├── CompetitionController.java # 竞赛 CRUD (/api/competitions)
│       │   │   ├── PortraitController.java # 画像 (radar/dashboard/trend/compare/distribution)
│       │   │   ├── UserController.java   # 用户管理 (/api/users)
│       │   │   ├── ScoreConfigController.java # 权重配置 (/api/score-config)
│       │   │   └── ExcelController.java  # 导入导出 (/api/excel)
│       │   ├── service/                  # 业务逻辑层 (共 9 个)
│       │   │   ├── AuthService.java      # 登录 (MD5 校验 + JWT 生成)
│       │   │   ├── VerticalProjectService.java  # 纵向项目 CRUD + 权限过滤 + 编号唯一性
│       │   │   ├── HorizontalProjectService.java # 横向项目 CRUD + 金额校验
│       │   │   ├── PatentService.java    # 专利 CRUD + 转让 (仅已授权)
│       │   │   ├── SoftwareCopyrightService.java # 软著 CRUD + 登记号唯一性
│       │   │   ├── PaperService.java     # 论文 CRUD + 收录标签关联 (@Transactional)
│       │   │   ├── CompetitionService.java # 竞赛 CRUD
│       │   │   ├── UserService.java      # 用户 CRUD + 密码重置 + 不可删除自己
│       │   │   └── ScoreCalculationService.java # 评分引擎 (5维计分 + 归一化 + 缓存)
│       │   ├── mapper/                   # MyBatis-Plus Mapper (共 10 个，全继承 BaseMapper)
│       │   │   ├── UserMapper.java / VerticalProjectMapper.java / HorizontalProjectMapper.java
│       │   │   ├── PatentMapper.java / PatentTransferMapper.java
│       │   │   ├── SoftwareCopyrightMapper.java / PaperMapper.java / PaperIndexMapper.java
│       │   │   ├── CompetitionMapper.java / ScoreConfigMapper.java
│       │   ├── entity/                   # 实体类 (共 10 个，与表一一对应)
│       │   │   ├── User.java / VerticalProject.java / HorizontalProject.java
│       │   │   ├── Patent.java / PatentTransfer.java
│       │   │   ├── SoftwareCopyright.java / Paper.java / PaperIndex.java
│       │   │   ├── Competition.java / ScoreConfig.java
│       │   ├── dto/                      # 请求 DTO (共 14 个)
│       │   │   ├── LoginDTO.java / UserDTO.java / ImportResultDTO.java
│       │   │   ├── VerticalProjectDTO.java + VerticalProjectQueryDTO.java
│       │   │   ├── HorizontalProjectDTO.java + HorizontalProjectQueryDTO.java
│       │   │   ├── PatentDTO.java + PatentQueryDTO.java + PatentTransferDTO.java
│       │   │   ├── SoftwareCopyrightDTO.java + SoftwareCopyrightQueryDTO.java
│       │   │   ├── PaperDTO.java + PaperQueryDTO.java
│       │   │   └── CompetitionDTO.java + CompetitionQueryDTO.java
│       │   ├── vo/                       # 响应 VO (共 11 个)
│       │   │   ├── LoginVO.java / VerticalProjectVO.java / HorizontalProjectVO.java
│       │   │   ├── PatentVO.java / PatentTransferVO.java / SoftwareCopyrightVO.java
│       │   │   ├── PaperVO.java / CompetitionVO.java
│       │   │   ├── PortraitRadarVO.java / PortraitDashboardVO.java
│       │   │   ├── PortraitTrendVO.java / PortraitDistributionVO.java
│       │   ├── interceptor/
│       │   │   └── JwtInterceptor.java   # JWT 拦截器 (Bearer Token 解析 + userId/role 注入)
│       │   └── util/
│       │       └── JwtUtil.java          # JWT 工具类 (生成/解析 Token, 24h 过期)
│       └── resources/
│           ├── application.yml           # Spring Boot 配置 (端口/数据库/编码)
│           └── db/migration/
│               └── V1__init.sql          # 数据库创建脚本 (10 表 + 初始数据)
│
├── portrait-web/                         # 前端项目 (Vue 3 + Vite)
│   ├── package.json                      # 依赖 (Element Plus/ECharts/Axios/Pinia/Vue Router)
│   ├── vite.config.js                    # Vite 配置 (端口 3000, /api 代理 -> localhost:8080)
│   └── src/
│       ├── main.js                       # 入口 (注册 Pinia/Router/ElementPlus)
│       ├── App.vue                       # 根组件
│       ├── api/                          # API 模块 (共 9 个)
│       │   ├── request.js                # Axios 封装 (拦截器: token + 401 重定向 + blob 支持)
│       │   ├── auth.js / verticalProject.js / horizontalProject.js
│       │   ├── patent.js / softwareCopyright.js / paper.js / competition.js
│       │   └── portrait.js               # 画像 6 接口封装
│       ├── router/index.js               # 路由配置 (13条, 全部懒加载) + 权限守卫
│       ├── store/user.js                 # Pinia 用户状态 (login/logout + 持久化)
│       ├── utils/token.js                # Token 持久化 (localStorage)
│       └── views/                        # 页面组件
│           ├── Layout.vue                # 主布局 (可折叠侧栏 + 顶栏)
│           ├── login/LoginView.vue        # 登录页
│           ├── dashboard/DashboardView.vue # 仪表盘首页
│           ├── project/                  # 科研项目
│           │   ├── VerticalProjectView.vue       # 纵向项目列表
│           │   ├── ProjectFormDialog.vue         # 纵向项目表单弹窗
│           │   ├── HorizontalProjectView.vue     # 横向项目列表
│           │   └── HorizontalProjectFormDialog.vue # 横向项目表单弹窗
│           ├── patent/                   # 专利管理
│           │   ├── PatentView.vue        # 专利列表 (类型/状态标签)
│           │   ├── PatentFormDialog.vue  # 专利表单弹窗
│           │   └── TransferDialog.vue    # 专利转让弹窗
│           ├── software/                 # 软著管理
│           │   ├── SoftwareView.vue      # 软著列表
│           │   └── SoftwareFormDialog.vue # 软著表单弹窗
│           ├── paper/                    # 论文管理
│           │   ├── PaperView.vue         # 论文列表 (8色收录标签)
│           │   └── PaperFormDialog.vue   # 论文表单弹窗 (收录标签多选)
│           ├── competition/              # 竞赛管理
│           │   ├── CompetitionView.vue   # 竞赛列表 (级别/等级双标签)
│           │   └── CompetitionFormDialog.vue # 竞赛表单弹窗
│           ├── portrait/                 # 数字画像
│           │   ├── PortraitView.vue      # 画像主页 (教师选择+6卡片)
│           │   ├── RadarChart.vue        # 雷达图 (5维 + 对比模式)
│           │   ├── TrendChart.vue        # 趋势图 (柱状+折线, 5模块Tab)
│           │   └── DistributionChart.vue # 分布图 (环形饼图, 4类切换)
│           └── admin/                    # 系统管理 (仅 ADMIN)
│               ├── UserManageView.vue    # 用户管理 (CRUD+密码重置)
│               ├── ScoreConfigView.vue   # 权重配置 (5维滑块+计分规则)
│               └── ImportExportView.vue  # 导入导出 (6模块Tab)
│
└── docs/                                 # 项目文档
    ├── 项目计划书.md
    ├── 项目需求文档.md
    ├── 数据库设计文档.md
    ├── DEVLOG.md                         # 开发日志
    ├── 测试报告-阶段六.md                 # 58 用例测试报告
    └── PROJECT.md                        # 本文档
```

---

## 二、数据库表结构

### 2.1 user (用户表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| work_no | VARCHAR(20) UNIQUE | 工号 |
| name | VARCHAR(50) | 姓名 |
| college | VARCHAR(100) | 学院 |
| role | VARCHAR(20) DEFAULT 'TEACHER' | 角色: TEACHER / ADMIN |
| password | VARCHAR(255) | 密码 (MD5) |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 2.2 vertical_project (纵向项目表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| name | VARCHAR(200) | 项目名称 |
| project_no | VARCHAR(100) UNIQUE | 项目编号 |
| level | VARCHAR(20) | 项目级别: 国家级/省部级/市厅级/校级 |
| source_unit | VARCHAR(200) | 来源单位 |
| start_date | DATE INDEX | 立项时间 |
| planned_end_date | DATE | 计划完成时间 |
| funding | DECIMAL(12,2) | 项目经费(万元) |
| role | VARCHAR(20) | 角色: 主持/参与 |
| status | VARCHAR(20) DEFAULT '在研' | 状态: 在研/已结题/延期 |
| remark | VARCHAR(500) | 备注 |

### 2.3 horizontal_project (横向项目表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| name | VARCHAR(200) | 项目名称 |
| company_name | VARCHAR(200) | 合作企业名称 |
| contract_amount | DECIMAL(12,2) CHECK >0 | 合同金额(万元) |
| sign_date | DATE | 签订日期 |
| end_date | DATE | 完成日期 |
| role | VARCHAR(20) | 角色: 主持/参与 |
| status | VARCHAR(20) DEFAULT '在研' | 状态: 在研/已结题 |

### 2.4 patent (专利表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| name | VARCHAR(200) | 专利名称 |
| type | VARCHAR(20) INDEX | 类型: 发明专利/实用新型/外观设计 |
| application_no | VARCHAR(100) | 专利申请号 |
| grant_no | VARCHAR(100) | 专利授权号 |
| application_date | DATE | 申请日期 |
| grant_date | DATE | 授权日期 |
| status | VARCHAR(20) INDEX | 状态: 申请中/已授权 |
| inventors | VARCHAR(500) | 发明人列表(分号分隔) |
| patentee | VARCHAR(200) | 专利权人 |
| is_counted | TINYINT(1) DEFAULT 1 | 是否计入考核 |
| remark | VARCHAR(500) | 备注 |

### 2.5 patent_transfer (专利转让记录表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| patent_id | BIGINT FK → patent.id ON DELETE CASCADE | 专利ID |
| transfer_date | DATE | 转让日期 |
| transferee | VARCHAR(200) | 受让方 |
| amount | DECIMAL(12,2) | 转让金额(万元) |

### 2.6 software_copyright (软著表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| name | VARCHAR(200) | 软件名称 |
| registration_no | VARCHAR(100) UNIQUE | 登记号 |
| version | VARCHAR(50) | 版本号 |
| dev_completion_date | DATE | 开发完成日期 |
| first_publish_date | DATE | 首次发表日期 |
| registration_date | DATE INDEX | 登记日期 |
| copyright_owners | VARCHAR(500) | 著作权人(分号分隔) |
| remark | VARCHAR(500) | 备注 |

### 2.7 paper (论文表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| title | VARCHAR(300) | 论文题目 |
| type | VARCHAR(20) | 类型: 期刊论文/会议论文 |
| journal_name | VARCHAR(200) | 期刊/会议名称 |
| volume | VARCHAR(50) | 卷号 |
| issue | VARCHAR(50) | 期号 |
| pages | VARCHAR(50) | 页码 |
| publish_date | DATE INDEX | 发表时间 |
| authors | VARCHAR(500) | 作者列表(分号分隔) |
| author_order | INT | 教师作者排序 |
| doi | VARCHAR(200) | DOI号 |
| remark | VARCHAR(500) | 备注 |

### 2.8 paper_index (论文收录关联表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| paper_id | BIGINT FK → paper.id ON DELETE CASCADE | 论文ID |
| index_type | VARCHAR(20) | 收录类型: SCI/SSCI/EI/CSCD/CSSCI/北大核心/普通期刊/其他 |

### 2.9 competition (竞赛指导表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| user_id | BIGINT INDEX | 教师ID |
| name | VARCHAR(200) | 竞赛名称 |
| organizer | VARCHAR(200) | 主办单位 |
| competition_date | DATE INDEX | 参赛时间 |
| student_team | VARCHAR(500) | 学生团队(分号分隔) |
| award_level | VARCHAR(20) INDEX | 获奖级别: 国家级/省级/校级 |
| award_grade | VARCHAR(20) | 获奖等级: 特等奖/一等奖/二等奖/三等奖/优秀奖 |
| guide_rank | INT DEFAULT 1 | 指导排名 |
| certificate_no | VARCHAR(100) | 获奖证书编号 |
| remark | VARCHAR(500) | 备注 |

### 2.10 score_config (评分权重配置表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK AUTO | 主键 |
| dimension | VARCHAR(20) UNIQUE | 维度名: 科研项目/专利成果/软件著作/学术论文/竞赛指导 |
| weight | DECIMAL(5,2) | 权重值(百分比) |
| scoring_rules | JSON | 计分规则 JSON |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

---

## 三、全部页面路由

| 路径 | 页面名称 | 功能说明 | 所需角色 |
|------|------|------|:--:|
| `/login` | 登录页 | 工号+密码登录 | 无 (noAuth) |
| `/dashboard` | 首页仪表盘 | 系统入口引导 | 登录即可 |
| `/project/vertical` | 纵向项目管理 | 列表+搜索+新增/编辑/删除 | 登录即可 |
| `/project/horizontal` | 横向项目管理 | 列表+搜索+新增/编辑/删除 | 登录即可 |
| `/patent` | 专利管理 | 列表+转让+新增/编辑/删除 | 登录即可 |
| `/software` | 软著管理 | 列表+搜索+新增/编辑/删除 | 登录即可 |
| `/paper` | 论文管理 | 列表+收录标签筛选+新增/编辑/删除 | 登录即可 |
| `/competition` | 竞赛指导管理 | 列表+搜索+新增/编辑/删除 | 登录即可 |
| `/portrait/:userId?` | 数字画像 | 雷达图+趋势图+分布图+仪表盘 | 登录即可 |
| `/admin/users` | 用户管理 | 用户CRUD+密码重置 | **ADMIN** |
| `/admin/score-config` | 权重配置 | 5维滑块+计分规则 | **ADMIN** |
| `/admin/import-export` | 数据导入导出 | 模板下载+批量导入+多条件导出 | **ADMIN** |

---

## 四、全部后端 API

### 4.1 认证模块

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| POST | `/api/auth/login` | 登录 (返回 JWT Token) | 无 |

### 4.2 纵向项目管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/vertical-projects` | 分页+多条件查询 | 登录 |
| GET | `/api/vertical-projects/{id}` | 详情 | 登录 (TEACHER 仅本人) |
| POST | `/api/vertical-projects` | 新增 | 登录 |
| PUT | `/api/vertical-projects/{id}` | 编辑 | 登录 (仅本人) |
| DELETE | `/api/vertical-projects/{id}` | 删除 | 登录 (仅本人) |

### 4.3 横向项目管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/horizontal-projects` | 分页+多条件查询 | 登录 |
| GET | `/api/horizontal-projects/{id}` | 详情 | 登录 |
| POST | `/api/horizontal-projects` | 新增 | 登录 |
| PUT | `/api/horizontal-projects/{id}` | 编辑 | 登录 |
| DELETE | `/api/horizontal-projects/{id}` | 删除 | 登录 |

### 4.4 专利管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/patents` | 分页+多条件查询 | 登录 |
| GET | `/api/patents/{id}` | 详情 | 登录 |
| POST | `/api/patents` | 新增 | 登录 |
| PUT | `/api/patents/{id}` | 编辑 | 登录 |
| DELETE | `/api/patents/{id}` | 删除 | 登录 |
| POST | `/api/patents/{id}/transfer` | **专利转让** (仅已授权) | 登录 |
| GET | `/api/patents/{id}/transfers` | 查询转让记录 | 登录 |

### 4.5 软著管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/software-copyrights` | 分页+条件查询 | 登录 |
| GET | `/api/software-copyrights/{id}` | 详情 | 登录 |
| POST | `/api/software-copyrights` | 新增 (登记号唯一性) | 登录 |
| PUT | `/api/software-copyrights/{id}` | 编辑 | 登录 |
| DELETE | `/api/software-copyrights/{id}` | 删除 | 登录 |

### 4.6 论文管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/papers` | 分页+多条件查询 (含收录标签筛选) | 登录 |
| GET | `/api/papers/{id}` | 详情 | 登录 |
| POST | `/api/papers` | 新增 (含收录标签) | 登录 |
| PUT | `/api/papers/{id}` | 编辑 (含收录标签) | 登录 |
| DELETE | `/api/papers/{id}` | 删除 (级联删除标签) | 登录 |

### 4.7 竞赛指导管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/competitions` | 分页+多条件查询 | 登录 |
| GET | `/api/competitions/{id}` | 详情 | 登录 |
| POST | `/api/competitions` | 新增 | 登录 |
| PUT | `/api/competitions/{id}` | 编辑 | 登录 |
| DELETE | `/api/competitions/{id}` | 删除 | 登录 |

### 4.8 数字画像

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/portrait/{userId}/radar` | 雷达图 (5维原始分+归一化) | 登录 (TEACHER 仅本人) |
| GET | `/api/portrait/{userId}/dashboard` | 仪表盘汇总 | 登录 (TEACHER 仅本人) |
| GET | `/api/portrait/{userId}/trend` | 近5年趋势 | 登录 (TEACHER 仅本人) |
| GET | `/api/portrait/{userId}/distribution` | 成果分布饼图 | 登录 (TEACHER 仅本人) |
| GET | `/api/portrait/compare?userIds=1,2` | **多教师对比** (2-5人) | **ADMIN** |
| GET | `/api/portrait/teachers` | 全院教师列表 | 登录 |

### 4.9 系统管理

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/users` | 用户列表 (分页+搜索) | **ADMIN** |
| POST | `/api/users` | 新增用户 | **ADMIN** |
| PUT | `/api/users/{id}` | 编辑用户 | **ADMIN** |
| DELETE | `/api/users/{id}` | 删除用户 (不可删除自己) | **ADMIN** |
| PUT | `/api/users/{id}/reset-password` | 重置密码为 123456 | **ADMIN** |
| GET | `/api/score-config` | 获取权重配置 | 登录 |
| PUT | `/api/score-config` | 更新权重 (总和=100%) | **ADMIN** |

### 4.10 导入导出

| 方法 | 路径 | 说明 | 角色 |
|------|------|------|:--:|
| GET | `/api/excel/template/{module}` | 下载导入模板 (6模块) | 登录 |
| POST | `/api/excel/import/{module}` | 批量导入 (逐行校验) | 登录 |
| GET | `/api/excel/export/{module}` | 多条件导出 (userId+year) | 登录 (TEACHER 仅本人) |

### 4.11 module 取值

`vertical-project` | `horizontal-project` | `patent` | `software` | `paper` | `competition`

---

## 五、权限实现方式

### 后端

```
JwtInterceptor (preHandle)
  ↓ 解析 Bearer Token → userId/workNo/role/name
  ↓ 注入 request.setAttribute()
  ↓
RoleAspect (@RequireRole)  ← Controller 方法级别校验 (如 /api/users)
  ↓
Service.buildQueryWrapper() ← 分页查询级权限过滤 (TEACHER eq userId)
  ↓
Service.getById/update/delete() ← 单条操作级所有权校验
```

### 前端

```
router.beforeEach()
  ↓ 无 token → /login
  ↓ meta.role 不匹配 → /dashboard
  ↓
Layout 侧栏 v-if="userStore.role === 'ADMIN'" ← 管理员菜单条件渲染
```

---

## 六、计分规则

### 6.1 科研项目

| 级别 | 主持 | 参与 |
|------|:--:|:--:|
| 国家级 | +20/项 | ×0.5 |
| 省部级 | +15/项 | ×0.5 |
| 市厅级 | +10/项 | ×0.5 |
| 校级 | +5/项 | ×0.5 |
| 横向项目 | 合同金额每10万+2分 | 折半 |

### 6.2 专利成果

| 类型 | 已授权 | 申请中 |
|------|:--:|:--:|
| 发明专利 | +20/项 | ×0.5 |
| 实用新型 | +10/项 | ×0.5 |
| 外观设计 | +5/项 | ×0.5 |
| 转让额外 | +5/项 | — |

### 6.3 软件著作

已登记 +10/项

### 6.4 学术论文

| 等级 | 收录标签 | 分值 | 非一作 |
|------|------|:--:|:--:|
| A 类 | SCI / SSCI | 25/篇 | ×0.5 |
| B 类 | EI / CSCD | 15/篇 | ×0.5 |
| C 类 | CSSCI / 北大核心 | 10/篇 | ×0.5 |
| D 类 | 普通期刊 / 其他 | 5/篇 | ×0.5 |

### 6.5 竞赛指导

| 等级 | 国家级 | 省级 | 校级 |
|------|:--:|:--:|:--:|
| 特等奖 | 30 | 20 | 10 |
| 一等奖 | 25 | 15 | 8 |
| 二等奖 | 20 | 10 | 5 |
| 三等奖 | 15 | 8 | 3 |
| 优秀奖 | — | — | — |
| 非第一指导 | ×0.7 | ×0.7 | ×0.7 |

---

## 七、部署配置

### docker-compose.yml

```yaml
services:
  mysql:
    image: mysql:8.0
    ports: ["3308:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: teacher_portrait
    command:
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
      - --character-set-client-handshake=FALSE
      - --skip-character-set-client-handshake
```

### application.yml (核心)

```yaml
server:
  port: 8080
  servlet:
    encoding:
      force: true
      charset: UTF-8
spring:
  datasource:
    url: jdbc:mysql://localhost:3308/teacher_portrait?...
    username: root
    password: root
```

### vite.config.js (代理)

```javascript
export default {
  server: { port: 3000, proxy: { '/api': { target: 'http://localhost:8080', changeOrigin: true } } }
}
```
