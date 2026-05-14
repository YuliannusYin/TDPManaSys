package com.portrait.controller;

import com.portrait.common.Result;
import com.portrait.common.BusinessException;
import com.portrait.entity.User;
import com.portrait.mapper.UserMapper;
import com.portrait.service.ScoreCalculationService;
import com.portrait.vo.PortraitDashboardVO;
import com.portrait.vo.PortraitDistributionVO;
import com.portrait.vo.PortraitRadarVO;
import com.portrait.vo.PortraitTrendVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "数字画像")
@RestController
@RequestMapping("/api/portrait")
public class PortraitController {

    @Resource
    private ScoreCalculationService scoreCalculationService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    @Operation(summary = "雷达图数据")
    @GetMapping("/{userId}/radar")
    public Result<PortraitRadarVO> radar(@PathVariable Long userId) {
        checkAccess(userId);
        return Result.success(scoreCalculationService.calculateRadar(userId));
    }

    @Operation(summary = "仪表盘汇总")
    @GetMapping("/{userId}/dashboard")
    public Result<PortraitDashboardVO> dashboard(@PathVariable Long userId) {
        checkAccess(userId);
        return Result.success(scoreCalculationService.calculateDashboard(userId));
    }

    @Operation(summary = "全院仪表盘汇总（管理员）")
    @GetMapping("/dashboard")
    public Result<PortraitDashboardVO> aggregatedDashboard() {
        if (!"ADMIN".equals(getCurrentUserRole())) {
            throw new BusinessException(403, "仅管理员可查看全院汇总");
        }
        return Result.success(scoreCalculationService.calculateAggregatedDashboard());
    }

    @Operation(summary = "历年趋势")
    @GetMapping("/{userId}/trend")
    public Result<List<PortraitTrendVO>> trend(@PathVariable Long userId) {
        checkAccess(userId);
        return Result.success(scoreCalculationService.calculateTrend(userId));
    }

    @Operation(summary = "成果分布")
    @GetMapping("/{userId}/distribution")
    public Result<PortraitDistributionVO> distribution(@PathVariable Long userId) {
        checkAccess(userId);
        return Result.success(scoreCalculationService.calculateDistribution(userId));
    }

    @Operation(summary = "多教师对比")
    @GetMapping("/compare")
    public Result<List<PortraitRadarVO>> compare(@RequestParam String userIds) {
        if (!"ADMIN".equals(getCurrentUserRole())) {
            throw new BusinessException(403, "仅管理员可使用对比功能");
        }
        List<Long> ids = Arrays.stream(userIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        if (ids.size() < 2 || ids.size() > 5) {
            throw new BusinessException("对比人数需在2-5人之间");
        }
        return Result.success(scoreCalculationService.compareRadars(ids));
    }

    @Operation(summary = "获取全院教师列表")
    @GetMapping("/teachers")
    public Result<List<User>> teachers() {
        List<User> list = userMapper.selectList(null);
        return Result.success(list);
    }

    private void checkAccess(Long userId) {
        if ("TEACHER".equals(getCurrentUserRole()) && !userId.equals(getCurrentUserId())) {
            throw new BusinessException(403, "无权查看他人数据");
        }
    }

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
}