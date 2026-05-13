package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.VerticalProjectDTO;
import com.portrait.dto.VerticalProjectQueryDTO;
import com.portrait.entity.User;
import com.portrait.entity.VerticalProject;
import com.portrait.mapper.UserMapper;
import com.portrait.mapper.VerticalProjectMapper;
import com.portrait.vo.VerticalProjectVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VerticalProjectService {

    @Resource
    private VerticalProjectMapper verticalProjectMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private ScoreCalculationService scoreCalculationService;

    public Page<VerticalProjectVO> page(VerticalProjectQueryDTO query) {
        LambdaQueryWrapper<VerticalProject> wrapper = buildQueryWrapper(query);
        Page<VerticalProject> page = verticalProjectMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> userIds = page.getRecords().stream()
                .map(VerticalProject::getUserId).distinct().collect(Collectors.toList());

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<VerticalProjectVO> voList = page.getRecords().stream().map(p -> {
            VerticalProjectVO vo = new VerticalProjectVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<VerticalProjectVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public VerticalProjectVO getById(Long id) {
        VerticalProject entity = verticalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }
        if ("TEACHER".equals(getCurrentUserRole()) && !entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权查看他人数据");
        }
        VerticalProjectVO vo = new VerticalProjectVO();
        BeanUtils.copyProperties(entity, vo);
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    @Transactional
    public VerticalProjectVO create(VerticalProjectDTO dto) {
        VerticalProject entity = new VerticalProject();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(getCurrentUserId());

        if (entity.getProjectNo() != null && !entity.getProjectNo().isEmpty()) {
            Long count = verticalProjectMapper.selectCount(
                    new LambdaQueryWrapper<VerticalProject>().eq(VerticalProject::getProjectNo, entity.getProjectNo()));
            if (count > 0) {
                throw new BusinessException("项目编号已存在");
            }
        }

        verticalProjectMapper.insert(entity);
        scoreCalculationService.clearMaxCache();
        return getById(entity.getId());
    }

    @Transactional
    public VerticalProjectVO update(Long id, VerticalProjectDTO dto) {
        VerticalProject entity = verticalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }
        if (!entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权修改他人数据");
        }

        if (dto.getProjectNo() != null && !dto.getProjectNo().isEmpty()
                && !dto.getProjectNo().equals(entity.getProjectNo())) {
            Long count = verticalProjectMapper.selectCount(
                    new LambdaQueryWrapper<VerticalProject>()
                            .eq(VerticalProject::getProjectNo, dto.getProjectNo())
                            .ne(VerticalProject::getId, id));
            if (count > 0) {
                throw new BusinessException("项目编号已存在");
            }
        }

        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        verticalProjectMapper.updateById(entity);
        scoreCalculationService.clearMaxCache();
        return getById(id);
    }

    @Transactional
    public void delete(Long id) {
        VerticalProject entity = verticalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }
        if (!entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权删除他人数据");
        }
        verticalProjectMapper.deleteById(id);
        scoreCalculationService.clearMaxCache();
    }

    private LambdaQueryWrapper<VerticalProject> buildQueryWrapper(VerticalProjectQueryDTO query) {
        LambdaQueryWrapper<VerticalProject> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(VerticalProject::getUserId, userId);
        }

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(VerticalProject::getName, query.getName());
        }
        if (query.getProjectNo() != null && !query.getProjectNo().isEmpty()) {
            wrapper.like(VerticalProject::getProjectNo, query.getProjectNo());
        }
        if (query.getLevel() != null && !query.getLevel().isEmpty()) {
            wrapper.eq(VerticalProject::getLevel, query.getLevel());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(VerticalProject::getStatus, query.getStatus());
        }
        if (query.getRole() != null && !query.getRole().isEmpty()) {
            wrapper.eq(VerticalProject::getRole, query.getRole());
        }
        if (query.getStartDateBegin() != null) {
            wrapper.ge(VerticalProject::getStartDate, query.getStartDateBegin());
        }
        if (query.getStartDateEnd() != null) {
            wrapper.le(VerticalProject::getStartDate, query.getStartDateEnd());
        }
        if (query.getFundingMin() != null) {
            wrapper.ge(VerticalProject::getFunding, query.getFundingMin());
        }
        if (query.getFundingMax() != null) {
            wrapper.le(VerticalProject::getFunding, query.getFundingMax());
        }

        wrapper.orderByDesc(VerticalProject::getStartDate);
        return wrapper;
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