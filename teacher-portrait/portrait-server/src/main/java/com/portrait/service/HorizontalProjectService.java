package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.HorizontalProjectDTO;
import com.portrait.dto.HorizontalProjectQueryDTO;
import com.portrait.entity.HorizontalProject;
import com.portrait.entity.User;
import com.portrait.mapper.HorizontalProjectMapper;
import com.portrait.mapper.UserMapper;
import com.portrait.vo.HorizontalProjectVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HorizontalProjectService {

    @Resource
    private HorizontalProjectMapper horizontalProjectMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    public Page<HorizontalProjectVO> page(HorizontalProjectQueryDTO query) {
        LambdaQueryWrapper<HorizontalProject> wrapper = buildQueryWrapper(query);
        Page<HorizontalProject> page = horizontalProjectMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> userIds = page.getRecords().stream()
                .map(HorizontalProject::getUserId).distinct().collect(Collectors.toList());

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<HorizontalProjectVO> voList = page.getRecords().stream().map(p -> {
            HorizontalProjectVO vo = new HorizontalProjectVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<HorizontalProjectVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public HorizontalProjectVO getById(Long id) {
        HorizontalProject entity = horizontalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }
        HorizontalProjectVO vo = new HorizontalProjectVO();
        BeanUtils.copyProperties(entity, vo);
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    public HorizontalProjectVO create(HorizontalProjectDTO dto) {
        if (dto.getContractAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("合同金额必须大于0");
        }

        HorizontalProject entity = new HorizontalProject();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(getCurrentUserId());
        horizontalProjectMapper.insert(entity);
        return getById(entity.getId());
    }

    public HorizontalProjectVO update(Long id, HorizontalProjectDTO dto) {
        HorizontalProject entity = horizontalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }

        if (dto.getContractAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("合同金额必须大于0");
        }

        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(getCurrentUserId());
        horizontalProjectMapper.updateById(entity);
        return getById(id);
    }

    public void delete(Long id) {
        HorizontalProject entity = horizontalProjectMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("项目不存在");
        }
        horizontalProjectMapper.deleteById(id);
    }

    private LambdaQueryWrapper<HorizontalProject> buildQueryWrapper(HorizontalProjectQueryDTO query) {
        LambdaQueryWrapper<HorizontalProject> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(HorizontalProject::getUserId, userId);
        }

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(HorizontalProject::getName, query.getName());
        }
        if (query.getCompanyName() != null && !query.getCompanyName().isEmpty()) {
            wrapper.like(HorizontalProject::getCompanyName, query.getCompanyName());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(HorizontalProject::getStatus, query.getStatus());
        }
        if (query.getRole() != null && !query.getRole().isEmpty()) {
            wrapper.eq(HorizontalProject::getRole, query.getRole());
        }
        if (query.getSignDateBegin() != null) {
            wrapper.ge(HorizontalProject::getSignDate, query.getSignDateBegin());
        }
        if (query.getSignDateEnd() != null) {
            wrapper.le(HorizontalProject::getSignDate, query.getSignDateEnd());
        }
        if (query.getAmountMin() != null) {
            wrapper.ge(HorizontalProject::getContractAmount, query.getAmountMin());
        }
        if (query.getAmountMax() != null) {
            wrapper.le(HorizontalProject::getContractAmount, query.getAmountMax());
        }

        wrapper.orderByDesc(HorizontalProject::getSignDate);
        return wrapper;
    }

    private Long getCurrentUserId() {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return userId;
    }
}