package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.CompetitionDTO;
import com.portrait.dto.CompetitionQueryDTO;
import com.portrait.entity.Competition;
import com.portrait.entity.User;
import com.portrait.mapper.CompetitionMapper;
import com.portrait.mapper.UserMapper;
import com.portrait.vo.CompetitionVO;
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
public class CompetitionService {

    @Resource
    private CompetitionMapper competitionMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    public Page<CompetitionVO> page(CompetitionQueryDTO query) {
        LambdaQueryWrapper<Competition> wrapper = buildQueryWrapper(query);
        Page<Competition> page = competitionMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> userIds = page.getRecords().stream()
                .map(Competition::getUserId).distinct().collect(Collectors.toList());

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<CompetitionVO> voList = page.getRecords().stream().map(p -> {
            CompetitionVO vo = new CompetitionVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<CompetitionVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public CompetitionVO getById(Long id) {
        Competition entity = competitionMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("竞赛记录不存在");
        }
        return toVO(entity);
    }

    public CompetitionVO create(CompetitionDTO dto) {
        Competition entity = new Competition();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getGuideRank() == null) {
            entity.setGuideRank(1);
        }
        entity.setUserId(getCurrentUserId());
        competitionMapper.insert(entity);
        return getById(entity.getId());
    }

    public CompetitionVO update(Long id, CompetitionDTO dto) {
        Competition entity = competitionMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("竞赛记录不存在");
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(getCurrentUserId());
        competitionMapper.updateById(entity);
        return getById(id);
    }

    public void delete(Long id) {
        Competition entity = competitionMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("竞赛记录不存在");
        }
        competitionMapper.deleteById(id);
    }

    private CompetitionVO toVO(Competition entity) {
        CompetitionVO vo = new CompetitionVO();
        BeanUtils.copyProperties(entity, vo);
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    private LambdaQueryWrapper<Competition> buildQueryWrapper(CompetitionQueryDTO query) {
        LambdaQueryWrapper<Competition> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(Competition::getUserId, userId);
        }

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(Competition::getName, query.getName());
        }
        if (query.getAwardLevel() != null && !query.getAwardLevel().isEmpty()) {
            wrapper.eq(Competition::getAwardLevel, query.getAwardLevel());
        }
        if (query.getAwardGrade() != null && !query.getAwardGrade().isEmpty()) {
            wrapper.eq(Competition::getAwardGrade, query.getAwardGrade());
        }
        if (query.getCompetitionDateBegin() != null) {
            wrapper.ge(Competition::getCompetitionDate, query.getCompetitionDateBegin());
        }
        if (query.getCompetitionDateEnd() != null) {
            wrapper.le(Competition::getCompetitionDate, query.getCompetitionDateEnd());
        }

        wrapper.orderByDesc(Competition::getCompetitionDate);
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