# 教师数字画像系统 — 项目全览

> AI 单文件快速上下文。阅读此文件即可深度了解整个项目，无需重新扫描代码。

---

## 1. 项目概要

| 项 | 内容 |
|------|------|
| **项目名称** | 教师数字画像系统 (Teacher Digital Portrait System) |
| **一句话简介** | 高校教师科研成果统一管理与可视化数字画像展示平台 |
| **后端** | Spring Boot 2.7.18 + MyBatis-Plus 3.5.3.1 + Knife4j 4.1.0 |
| **安全** | JWT 认证 (jjwt 0.9.1) + AOP 角色注解 + Service 层权重校验 |
| **前端** | Vue 3.3.4 + Element Plus 2.3.12 + ECharts 6.0.0 + Pinia 2.1.6 |
| **构建** | Maven Wrapper (后端) / Vite 4.4.9 (前端) |
| **数据库** | MySQL 8.0 (Docker, 映射端口 3308) |
| **编码** | 全局 UTF-8 (数据库 utf8mb4_unicode_ci, JDBC, Spring, Vite 代理) |
| **项目根目录** | `e:\B_ProjSourCodeLibrary\Projects\Project-Study-00\TDPManaSys\teacher-portrait` |

---

## 2. 开发进度

| 阶段 | 内容 | 状态 |
|:----:|------|:----:|
| 阶段一 | 项目初始化、登录认证、路由守卫、权限拦截 | ✅ |
| 阶段二 | 6 个成果管理模块完整 CRUD + 审计修复 | ✅ |
| 阶段三 | 数字画像与可视化（评分引擎 + 4 类图表） | ✅ |
| 阶段四 | 系统管理与配置（用户管理 / 权重配置） | ⬜ |
| 阶段五 | Excel 导入导出 | ⬜ |
| 阶段六 | 测试与优化 | ⬜ |

---

## 3. 已完成功能清单

### 3.1 登录认证

| 层 | 文件路径 |
|------|------|
| Controller | `portrait-server/src/main/java/com/portrait/controller/AuthController.java` |
| Service | `portrait-server/src/main/java/com/portrait/service/AuthService.java` |
| 前端页面 | `portrait-web/src/views/login/LoginView.vue` |

- `POST /api/auth/login` — 工号 + MD5 密码 → JWT Token（24h 过期，含 userId/workNo/role/name）
- 前端 Pinia store 存储 Token → axios 拦截器自动携带 `Authorization: Bearer <token>`
- 响应拦截器 `code=401` 自动清除 Token 并跳转登录页

### 3.2 纵向项目管理 (VerticalProject)

| 层 | 文件路径 |
|------|------|
| Entity | `src/main/java/com/portrait/entity/VerticalProject.java` (10 字段) |
| Mapper | `src/main/java/com/portrait/mapper/VerticalProjectMapper.java` |
| DTO | `dto/VerticalProjectDTO.java` (11 字段含校验) + `dto/VerticalProjectQueryDTO.java` (9 筛选) |
| VO | `vo/VerticalProjectVO.java` (含 teacherName/teacherCollege) |
| Service | `service/VerticalProjectService.java` (分页+7条件+编号唯一性+权限) |
| Controller | `controller/VerticalProjectController.java` (5 REST 端点) |
| API 前端 | `portrait-web/src/api/verticalProject.js` |
| 列表页 | `portrait-web/src/views/project/VerticalProjectView.vue` (搜索+表格+分页) |
| 表单弹窗 | `portrait-web/src/views/project/ProjectFormDialog.vue` |

接口：`GET /api/vertical-projects` (分页) | `POST` | `PUT /{id}` | `DELETE /{id}` | `GET /{id}`  
枚举：level(国家级/省部级/市厅级/校级) | role(主持/参与) | status(在研/已结题/延期)  
唯一校验：`projectNo`（新增查重，编辑排除自身）

### 3.3 横向项目管理 (HorizontalProject)

| 层 | 文件路径 |
|------|------|
| Service | `service/HorizontalProjectService.java` |
| Controller | `controller/HorizontalProjectController.java` |
| 前端 | `views/project/HorizontalProjectView.vue` + `HorizontalProjectFormDialog.vue` |

接口：`GET /api/horizontal-projects` | `POST` | `PUT /{id}` | `DELETE /{id}` | `GET /{id}`  
校验：`contractAmount` @DecimalMin("0.01") (DTO + Service 双重)

### 3.4 专利管理 (Patent) + 转让 (PatentTransfer)

| 层 | 文件路径 |
|------|------|
| Service | `service/PatentService.java` (CRUD + transfer + getTransfers) |
| Controller | `controller/PatentController.java` (7 端点) |
| 前端 | `views/patent/PatentView.vue` + `PatentFormDialog.vue` + `TransferDialog.vue` |

接口：`GET /api/patents` | `POST` | `PUT /{id}` | `DELETE /{id}` | `GET /{id}` | `POST /{id}/transfer` | `GET /{id}/transfers`  
枚举：type(发明专利/实用新型/外观设计) | status(申请中/已授权)  
转让限制：仅"已授权"专利可转让 (`@Transactional`)

### 3.5 软著管理 (SoftwareCopyright)

| 层 | 文件路径 |
|------|------|
| Service | `service/SoftwareCopyrightService.java` |
| Controller | `controller/SoftwareCopyrightController.java` |
| 前端 | `views/software/SoftwareView.vue` + `SoftwareFormDialog.vue` |

接口：`GET /api/software-copyrights` | `POST` | `PUT /{id}` | `DELETE /{id}` | `GET /{id}`  
唯一校验：`registrationNo`（**最规范的实现模板** — 独立方法 `checkRegistrationNoUnique(no, excludeId)`）

### 3.6 论文管理 (Paper) + 收录标签 (PaperIndex)

| 层 | 文件路径 |
|------|------|
| Service | `service/PaperService.java` (`@Transactional` 全量事务) |
| Controller | `controller/PaperController.java` |
| 前端 | `views/paper/PaperView.vue` + `PaperFormDialog.vue` |

接口：`GET /api/papers` | `POST` (含 indexTypes[]) | `PUT /{id}` | `DELETE /{id}` (级联删标签) | `GET /{id}`  
收录标签：SCI/SSCI→A类, EI/CSCD→B类, CSSCI/北大核心→C类, 其他→D类  
筛选：收录标签逗号分隔 → PaperIndex IN 查询 → 反查 paperId

### 3.7 竞赛指导管理 (Competition)

| 层 | 文件路径 |
|------|------|
| Service | `service/CompetitionService.java` |
| Controller | `controller/CompetitionController.java` |
| 前端 | `views/competition/CompetitionView.vue` + `CompetitionFormDialog.vue` |

接口：`GET /api/competitions` | `POST` | `PUT /{id}` | `DELETE /{id}` | `GET /{id}`  
枚举：awardLevel(国家级/省级/校级) | awardGrade(特等奖/一等/二等/三等/优秀奖)

### 3.8 数字画像与可视化 (阶段三)

| 层 | 文件路径 |
|------|------|
| 评分引擎 | `service/ScoreCalculationService.java` (75 源文件, 5 维计分 + 归一化 + 趋势 + 分布) |
| Controller | `controller/PortraitController.java` (6 端点) |
| 前端主页 | `views/portrait/PortraitView.vue` (6 概览卡片 + 图表布局) |
| 雷达图 | `views/portrait/RadarChart.vue` (ECharts, 原始/归一切换, 对比模式) |
| 趋势图 | `views/portrait/TrendChart.vue` (5 模块 Tab, 柱状+折线双轴) |
| 分布图 | `views/portrait/DistributionChart.vue` (环形饼图, 4 类切换) |

接口：

| 方法 | 路径 | 权限 |
|------|------|------|
| GET | `/api/portrait/{userId}/radar` | TEACHER 仅本人 |
| GET | `/api/portrait/{userId}/dashboard` | TEACHER 仅本人 |
| GET | `/api/portrait/{userId}/trend` | TEACHER 仅本人 |
| GET | `/api/portrait/{userId}/distribution` | TEACHER 仅本人 |
| GET | `/api/portrait/compare?userIds=` | **仅 ADMIN** (2-5 人) |
| GET | `/api/portrait/teachers` | 全院教师下拉数据源 |

计分规则详见：`portrait-server/src/main/java/com/portrait/service/ScoreCalculationService.java`
归一化缓存：`volatile cachedGlobalMaxes` + `clearMaxCache()` 刷新接口

---

## 4. 数据库核心信息

### 4.1 表清单

| 表名 | 用途 | 关键字段 |
|------|------|------|
| `user` | 用户表 | `work_no` UNIQUE, `role` (TEACHER/ADMIN), `password` MD5 |
| `vertical_project` | 纵向项目 | `user_id` FK, `project_no` UNIQUE, 枚举 level/role/status |
| `horizontal_project` | 横向项目 | `user_id` FK, CHECK(`contract_amount` > 0) |
| `patent` | 专利 | `user_id` FK, 枚举 type/status, `is_counted` 考核标记 |
| `patent_transfer` | 专利转让 | `patent_id` FK CASCADE, `amount` 转让金额 |
| `software_copyright` | 软著 | `user_id` FK, `registration_no` UNIQUE |
| `paper` | 论文 | `user_id` FK, `author_order` 作者排序 |
| `paper_index` | 论文收录 | `paper_id` FK CASCADE, `index_type` 收录类型 |
| `competition` | 竞赛指导 | `user_id` FK, 枚举 award_level/award_grade |
| `score_config` | 权重配置 | `dimension` UNIQUE, `weight`, `scoring_rules` JSON |

### 4.2 测试账号

| 角色 | 工号 | 密码 | 姓名 | 学院 |
|------|------|------|------|------|
| 管理员 | admin | 123456 | 系统管理员 | 信息中心 |
| 教师 | T001 | 123456 | 张教授 | 计算机学院 |
| 教师 | T002 | 123456 | 李教授 | 计算机学院 |

> 密码存储：`MD5('123456')`，JWT 过期 24h (86400000ms)

---

## 5. API 接口清单

### 5.1 认证

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/api/auth/login` | 公开 | 工号+密码 → Token |

### 5.2 成果管理（6 模块 × 5 接口 = 30 端点）

每个模块统一模式：

| 方法 | 路径 | 示例 (纵向项目) |
|------|------|------|
| GET | `/api/{module}` | `/api/vertical-projects?page=1&size=10&level=国家级` |
| GET | `/api/{module}/{id}` | `/api/vertical-projects/1` |
| POST | `/api/{module}` | `/api/vertical-projects` (JSON Body) |
| PUT | `/api/{module}/{id}` | `/api/vertical-projects/1` (JSON Body) |
| DELETE | `/api/{module}/{id}` | `/api/vertical-projects/1` |

模块路由表：

| 模块 | 路径前缀 | 权限过滤 |
|------|----------|------|
| 纵向项目 | `/api/vertical-projects` | TEACHER 只看本人数据 |
| 横向项目 | `/api/horizontal-projects` | TEACHER 只看本人数据 |
| 专利 | `/api/patents` | TEACHER 只看本人数据 |
| 软著 | `/api/software-copyrights` | TEACHER 只看本人数据 |
| 论文 | `/api/papers` | TEACHER 只看本人数据 |
| 竞赛 | `/api/competitions` | TEACHER 只看本人数据 |

### 5.3 专利额外接口

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/api/patents/{id}/transfer` | 登录即可 | 仅已授权专利可转让 |
| GET | `/api/patents/{id}/transfers` | 登录即可 | 查询转让记录 |

### 5.4 数字画像（6 端点）

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/api/portrait/{userId}/radar` | TEACHER 仅本人 | 五维雷达图 |
| GET | `/api/portrait/{userId}/dashboard` | TEACHER 仅本人 | 仪表盘汇总 |
| GET | `/api/portrait/{userId}/trend` | TEACHER 仅本人 | 近5年趋势 |
| GET | `/api/portrait/{userId}/distribution` | TEACHER 仅本人 | 四类饼图分布 |
| GET | `/api/portrait/compare?userIds=2,3` | **仅 ADMIN** | 多人对比 |
| GET | `/api/portrait/teachers` | 登录即可 | 教师列表 |

---

## 6. 关键架构约定

### 6.1 后端分层

```
Controller (REST 端点, 参数校验 @Valid)
    ↓
Service (业务逻辑, 权限过滤, 唯一性校验)
    ↓
Mapper (MyBatis-Plus BaseMapper, 继承即用)
    ↓
Entity (表映射, @TableName, @TableId, @TableField)
```

- **DTO**：`dto/` 包，接收前端请求体，含 `@NotBlank`/`@NotNull`/`@DecimalMin` 校验
- **VO**：`vo/` 包，返回给前端，含关联数据（如 teacherName/teacherCollege）
- **统一响应**：`Result<T>` — `{code:200, message:"操作成功", data: T}`
- **分页响应**：`PageResult<T>` — `{total, page, size, records: [...]}`
- **异常处理**：`BusinessException(code, message)` → `GlobalExceptionHandler` → `Result.error(code, message)`

### 6.2 统一响应格式

```java
// 成功
Result.success(data)    → {"code":200,"message":"操作成功","data":...}
// 失败
Result.error(401,"msg") → {"code":401,"message":"msg","data":null}
// 分页
Result.success(PageResult.of(page)) → {"code":200,"data":{"total":N,"page":1,"size":10,"records":[...]}}
```

### 6.3 权限控制

三级权限体系：

| 层级 | 机制 | 文件 |
|------|------|------|
| **认证** | JwtInterceptor 拦截 `/api/**`，解析 Bearer Token → 注入 request attributes | `interceptor/JwtInterceptor.java` (排除 `/api/auth/login`, `/doc.html`, `/swagger-ui/**` 等) |
| **列表隔离** | Service 层 `buildQueryWrapper()` — TEACHER 自动 `eq(user_id, currentUserId)` | 各 Service |
| **写操作隔离** | update/delete/getById 开头校验 `entity.getUserId().equals(currentUserId)` | 各 Service |
| **特殊接口** | AOP `@RequireRole("ADMIN")` 注解 + `RoleAspect` 切面 | `annotation/RequireRole.java` + `aspect/RoleAspect.java` |

### 6.4 Token 解析注入的 Request Attributes

```java
// JwtInterceptor 从 Token 中解析后设置：
request.setAttribute("userId", claims.get("userId", Long.class));
request.setAttribute("workNo", claims.get("workNo", String.class));
request.setAttribute("role", claims.get("role", String.class));
request.setAttribute("name", claims.get("name", String.class));
```

### 6.5 前端路由

| 路径 | 组件 | 权限 |
|------|------|------|
| `/login` | LoginView.vue | 公开 (noAuth) |
| `/dashboard` | DashboardView.vue | 登录 |
| `/project/vertical` | VerticalProjectView.vue | 登录 |
| `/project/horizontal` | HorizontalProjectView.vue | 登录 |
| `/patent` | PatentView.vue | 登录 |
| `/software` | SoftwareView.vue | 登录 |
| `/paper` | PaperView.vue | 登录 |
| `/competition` | CompetitionView.vue | 登录 |
| `/portrait/:userId?` | PortraitView.vue | 登录 |
| `/admin/users` | UserManageView.vue | **仅 ADMIN** |
| `/admin/score-config` | ScoreConfigView.vue | **仅 ADMIN** |
| `/admin/import-export` | ImportExportView.vue | **仅 ADMIN** |

- 路由守卫 `beforeEach`：无 Token → `/login`；角色不匹配 meta.role → `/dashboard`
- Layout.vue 侧栏：管理员菜单 `v-if="userStore.role === 'ADMIN'"` 条件渲染

### 6.6 前端组件树

```
App.vue
├── LoginView.vue            (/login)
└── Layout.vue               (主布局)
    ├── DashboardView.vue    (/dashboard)
    ├── VerticalProjectView.vue      + ProjectFormDialog.vue
    ├── HorizontalProjectView.vue    + HorizontalProjectFormDialog.vue
    ├── PatentView.vue               + PatentFormDialog.vue + TransferDialog.vue
    ├── SoftwareView.vue             + SoftwareFormDialog.vue
    ├── PaperView.vue                + PaperFormDialog.vue
    ├── CompetitionView.vue          + CompetitionFormDialog.vue
    ├── PortraitView.vue             + RadarChart.vue + TrendChart.vue + DistributionChart.vue
    ├── UserManageView.vue            (仅 ADMIN, 占位)
    ├── ScoreConfigView.vue           (仅 ADMIN, 占位)
    └── ImportExportView.vue          (仅 ADMIN, 占位)
```

### 6.7 前端 API 封装

```javascript
// portrait-web/src/api/request.js
import axios from 'axios'
import { getToken, removeToken } from '../utils/token'

const service = axios.create({
  baseURL: '/api',    // Vite proxy → localhost:8080
  timeout: 15000
})

// 请求拦截：自动注入 Bearer Token
service.interceptors.request.use(config => {
  const token = getToken()
  if (token) config.headers['Authorization'] = 'Bearer ' + token
  return config
})

// 响应拦截：code !== 200 → ElMessage.error; code=401 → 清除 Token 跳 /login
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) { /* 清除 token, 跳 /login */ }
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => { /* 处理 HTTP 401 */ }
)
```

---

## 7. 重要代码模式

### 7.1 获取当前用户

```java
// 所有 Service 中的标准模板
@Resource
private HttpServletRequest request;

private Long getCurrentUserId() {
    Long userId = (Long) request.getAttribute("userId");
    if (userId == null) {
        throw new BusinessException(401, "未登录");
    }
    return userId;
}

private String getCurrentUserRole() {
    return (String) request.getAttribute("role");
}
```

### 7.2 分页查询 + 权限过滤

```java
// Service.page() 标准模式
public Page<XxxVO> page(XxxQueryDTO query) {
    LambdaQueryWrapper<Xxx> wrapper = new LambdaQueryWrapper<>();

    // ★ 权限过滤（所有模块统一模式）
    String role = (String) request.getAttribute("role");
    Long userId = (Long) request.getAttribute("userId");
    if ("TEACHER".equals(role)) {
        wrapper.eq(Xxx::getUserId, userId);
    }

    // 条件筛选
    if (query.getName() != null && !query.getName().isEmpty()) {
        wrapper.like(Xxx::getName, query.getName());
    }
    // ... 更多条件 ...

    wrapper.orderByDesc(Xxx::getCreateTime);

    Page<Xxx> page = mapper.selectPage(new Page<>(query.getPage(), query.getSize()), wrapper);

    // ★ 批量查用户名（空列表安全处理）
    List<Long> userIds = page.getRecords().stream()
            .map(Xxx::getUserId).distinct().collect(Collectors.toList());
    Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
            : userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, Function.identity()));

    // ★ 组装 VO（填充 teacherName/teacherCollege）
    List<XxxVO> voList = page.getRecords().stream().map(p -> {
        XxxVO vo = new XxxVO();
        BeanUtils.copyProperties(p, vo);
        User user = userMap.get(p.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }).collect(Collectors.toList());

    Page<XxxVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
    voPage.setRecords(voList);
    return voPage;
}
```

### 7.3 所有权校验（update/delete/getById）

```java
// 所有 Service 的 update()/delete()/getById() 统一模式（安全修复后）
public XxxVO update(Long id, XxxDTO dto) {
    Xxx entity = mapper.selectById(id);
    if (entity == null) {
        throw new BusinessException("记录不存在");
    }
    // ★ 所有权校验
    if (!entity.getUserId().equals(getCurrentUserId())) {
        throw new BusinessException("无权修改他人数据");
    }
    BeanUtils.copyProperties(dto, entity);
    entity.setId(id);
    // 注意：update 中不再 setUserId
    mapper.updateById(entity);
    return getById(id);
}

public void delete(Long id) {
    Xxx entity = mapper.selectById(id);
    if (entity == null) {
        throw new BusinessException("记录不存在");
    }
    // ★ 所有权校验
    if (!entity.getUserId().equals(getCurrentUserId())) {
        throw new BusinessException("无权删除他人数据");
    }
    mapper.deleteById(id);
}

public XxxVO getById(Long id) {
    Xxx entity = mapper.selectById(id);
    if (entity == null) {
        throw new BusinessException("记录不存在");
    }
    // ★ TEACHER 跨用户查看校验
    if ("TEACHER".equals(getCurrentUserRole())
            && !entity.getUserId().equals(getCurrentUserId())) {
        throw new BusinessException("无权查看他人数据");
    }
    // 组装 VO 返回...
}
```

### 7.4 唯一性校验（参考模板）

```java
// 来源：SoftwareCopyrightService.java — 最规范的唯一性校验实现
private void checkRegistrationNoUnique(String registrationNo, Long excludeId) {
    LambdaQueryWrapper<SoftwareCopyright> wrapper =
        new LambdaQueryWrapper<SoftwareCopyright>()
            .eq(SoftwareCopyright::getRegistrationNo, registrationNo);
    if (excludeId != null) {
        wrapper.ne(SoftwareCopyright::getId, excludeId);  // 编辑时排除自身
    }
    Long count = softwareCopyrightMapper.selectCount(wrapper);
    if (count > 0) {
        throw new BusinessException("登记号已存在");
    }
}

// 调用：
// 新增时：checkRegistrationNoUnique(dto.getRegistrationNo(), null);
// 编辑时：checkRegistrationNoUnique(dto.getRegistrationNo(), id);
```

### 7.5 归一化缓存（DCL 模式）

```java
// 来源：ScoreCalculationService.java
private volatile Map<String, BigDecimal> cachedGlobalMaxes;

private Map<String, BigDecimal> getGlobalMaxes() {
    if (cachedGlobalMaxes != null) return cachedGlobalMaxes;
    synchronized (this) {
        if (cachedGlobalMaxes != null) return cachedGlobalMaxes;
        // 全量计算并缓存
        Map<String, BigDecimal> globalMaxes = new LinkedHashMap<>();
        // ... 遍历用户计算各维度最大值 ...
        cachedGlobalMaxes = globalMaxes;
        return globalMaxes;
    }
}

public void clearMaxCache() {  // 数据变更后调用
    cachedGlobalMaxes = null;
}
```

### 7.6 Controller 标准模板

```java
@Tag(name = "模块名")
@RestController
@RequestMapping("/api/xxx")
public class XxxController {

    @Resource
    private XxxService service;

    @Operation(summary = "分页查询")
    @GetMapping
    public Result<PageResult<XxxVO>> page(XxxQueryDTO query) {
        return Result.success(PageResult.of(service.page(query)));
    }

    @Operation(summary = "获取详情")
    @GetMapping("/{id}")
    public Result<XxxVO> getById(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @Operation(summary = "新增")
    @PostMapping
    public Result<XxxVO> create(@Valid @RequestBody XxxDTO dto) {
        return Result.success(service.create(dto));
    }

    @Operation(summary = "编辑")
    @PutMapping("/{id}")
    public Result<XxxVO> update(@PathVariable Long id, @Valid @RequestBody XxxDTO dto) {
        return Result.success(service.update(id, dto));
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }
}
```

---

## 8. 部署信息

### Docker MySQL

```yaml
# docker-compose.yml
services:
  mysql:
    image: mysql:8.0
    container_name: teacher-portrait-mysql
    ports: ["3308:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: teacher_portrait
    command:
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
      - --skip-character-set-client-handshake   # 强制客户端 utf8mb4
      - --default-time-zone=+08:00
```

### 后端配置

```yaml
# application.yml
server.port: 8080
server.servlet.encoding.charset: UTF-8
server.servlet.encoding.force: true

spring.datasource.url: jdbc:mysql://localhost:3308/teacher_portrait?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&connectionCollation=utf8mb4_unicode_ci
spring.datasource.username: root
spring.datasource.password: root

jwt.secret: teacher-portrait-secret-key-2026-very-long-secret-key-for-jwt
jwt.expiration: 86400000

mybatis-plus.configuration.map-underscore-to-camel-case: true
mybatis-plus.global-config.db-config.id-type: auto
```

### 前端代理

```javascript
// vite.config.js
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 9. 项目文件统计

| 指标 | 数量 |
|------|:--:|
| 后端 Java 源文件 | 75 |
| 后端 Service | 8 (Auth + 6 业务 + ScoreCalculation) |
| 后端 Controller | 8 |
| 数据库表 | 10 |
| REST API 端点 | 39 |
| 前端页面/组件 | 25 |
| 前端 Chart 组件 | 4 (ECharts) |

---

## 10. 待开发内容

### 阶段四 — 系统管理

- [ ] 用户管理 CRUD（UserManageView.vue 当前占位）
- [ ] 权重配置管理（ScoreConfigView.vue 当前占位）

### 阶段五 — Excel 导入导出

- [ ] 各模块数据导出（按筛选条件导出 Excel）
- [ ] 模板下载 + 批量导入

### 阶段六 — 测试与优化

- [ ] 功能测试全覆盖
- [ ] 权限回归测试
- [ ] 前端 ECharts 异步加载优化（PortraitView.js 1.13MB）

---

## 11. 已知技术债务

| 优先级 | 问题 | 影响范围 |
|:--:|------|------|
| 🟡 高 | 唯一性校验缺失：横向项目(无)、专利(申请号/授权号)、论文(DOI)、竞赛(证书编号) | 5 模块 |
| 🟢 低 | 4 个模块写操作缺少 `@Transactional` | Vertical/Horizontal/Software/Competition |
| 🟢 低 | PaperService 索引标签逐条 insert → 应批量 | 1 模块 |
| 🟢 低 | PaperService indexTypes 类型不一致 (DTO List vs QueryDTO String) | 1 模块 |
| 🟢 低 | 归一化缓存 `clearMaxCache()` 未集成到 CRUD 数据变更流程 | 1 模块 |
| 🟢 低 | ECharts 打包 chunk 较大 (PortraitView.js 1.13MB) | 1 文件 |
| 🟢 低 | 管理员页面仅占位 | 3 页面 |
