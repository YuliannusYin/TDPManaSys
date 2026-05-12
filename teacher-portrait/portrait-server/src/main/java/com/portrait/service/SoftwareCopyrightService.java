package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.SoftwareCopyrightDTO;
import com.portrait.dto.SoftwareCopyrightQueryDTO;
import com.portrait.entity.SoftwareCopyright;
import com.portrait.entity.User;
import com.portrait.mapper.SoftwareCopyrightMapper;
import com.portrait.mapper.UserMapper;
import com.portrait.vo.SoftwareCopyrightVO;
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
public class SoftwareCopyrightService {

    @Resource
    private SoftwareCopyrightMapper softwareCopyrightMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    public Page<SoftwareCopyrightVO> page(SoftwareCopyrightQueryDTO query) {
        LambdaQueryWrapper<SoftwareCopyright> wrapper = buildQueryWrapper(query);
        Page<SoftwareCopyright> page = softwareCopyrightMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> userIds = page.getRecords().stream()
                .map(SoftwareCopyright::getUserId).distinct().collect(Collectors.toList());

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<SoftwareCopyrightVO> voList = page.getRecords().stream().map(p -> {
            SoftwareCopyrightVO vo = new SoftwareCopyrightVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<SoftwareCopyrightVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public SoftwareCopyrightVO getById(Long id) {
        SoftwareCopyright entity = softwareCopyrightMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("软著不存在");
        }
        return toVO(entity);
    }

    public SoftwareCopyrightVO create(SoftwareCopyrightDTO dto) {
        checkRegistrationNoUnique(dto.getRegistrationNo(), null);

        SoftwareCopyright entity = new SoftwareCopyright();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(getCurrentUserId());
        softwareCopyrightMapper.insert(entity);
        return getById(entity.getId());
    }

    public SoftwareCopyrightVO update(Long id, SoftwareCopyrightDTO dto) {
        SoftwareCopyright entity = softwareCopyrightMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("软著不存在");
        }

        checkRegistrationNoUnique(dto.getRegistrationNo(), id);

        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(getCurrentUserId());
        softwareCopyrightMapper.updateById(entity);
        return getById(id);
    }

    public void delete(Long id) {
        SoftwareCopyright entity = softwareCopyrightMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("软著不存在");
        }
        softwareCopyrightMapper.deleteById(id);
    }

    private void checkRegistrationNoUnique(String registrationNo, Long excludeId) {
        LambdaQueryWrapper<SoftwareCopyright> wrapper = new LambdaQueryWrapper<SoftwareCopyright>()
                .eq(SoftwareCopyright::getRegistrationNo, registrationNo);
        if (excludeId != null) {
            wrapper.ne(SoftwareCopyright::getId, excludeId);
        }
        Long count = softwareCopyrightMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("登记号已存在");
        }
    }

    private SoftwareCopyrightVO toVO(SoftwareCopyright entity) {
        SoftwareCopyrightVO vo = new SoftwareCopyrightVO();
        BeanUtils.copyProperties(entity, vo);
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    private LambdaQueryWrapper<SoftwareCopyright> buildQueryWrapper(SoftwareCopyrightQueryDTO query) {
        LambdaQueryWrapper<SoftwareCopyright> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(SoftwareCopyright::getUserId, userId);
        }

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(SoftwareCopyright::getName, query.getName());
        }
        if (query.getRegistrationDateBegin() != null) {
            wrapper.ge(SoftwareCopyright::getRegistrationDate, query.getRegistrationDateBegin());
        }
        if (query.getRegistrationDateEnd() != null) {
            wrapper.le(SoftwareCopyright::getRegistrationDate, query.getRegistrationDateEnd());
        }

        wrapper.orderByDesc(SoftwareCopyright::getRegistrationDate);
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