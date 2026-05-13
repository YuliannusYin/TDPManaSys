package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.PaperDTO;
import com.portrait.dto.PaperQueryDTO;
import com.portrait.entity.Paper;
import com.portrait.entity.PaperIndex;
import com.portrait.entity.User;
import com.portrait.mapper.PaperIndexMapper;
import com.portrait.mapper.PaperMapper;
import com.portrait.mapper.UserMapper;
import com.portrait.vo.PaperVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaperService {

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private PaperIndexMapper paperIndexMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private ScoreCalculationService scoreCalculationService;

    public Page<PaperVO> page(PaperQueryDTO query) {
        LambdaQueryWrapper<Paper> wrapper = buildQueryWrapper(query);

        List<String> queryIndexTypes = parseIndexTypes(query.getIndexTypes());
        if (!queryIndexTypes.isEmpty()) {
            List<PaperIndex> indexes = paperIndexMapper.selectList(
                    new LambdaQueryWrapper<PaperIndex>().in(PaperIndex::getIndexType, queryIndexTypes));
            Set<Long> paperIdsFromIndex = indexes.stream().map(PaperIndex::getPaperId).collect(Collectors.toSet());
            if (paperIdsFromIndex.isEmpty()) {
                Page<PaperVO> emptyPage = new Page<>(query.getPage(), query.getSize(), 0);
                emptyPage.setRecords(Collections.emptyList());
                return emptyPage;
            }
            wrapper.in(Paper::getId, paperIdsFromIndex);
        }

        Page<Paper> page = paperMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> paperIds = page.getRecords().stream()
                .map(Paper::getId).collect(Collectors.toList());

        Map<Long, List<String>> indexMap = paperIds.isEmpty() ? Collections.emptyMap()
                : paperIndexMapper.selectList(
                        new LambdaQueryWrapper<PaperIndex>().in(PaperIndex::getPaperId, paperIds))
                        .stream().collect(Collectors.groupingBy(
                                PaperIndex::getPaperId, Collectors.mapping(PaperIndex::getIndexType, Collectors.toList())));

        List<Long> userIds = page.getRecords().stream()
                .map(Paper::getUserId).distinct().collect(Collectors.toList());
        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<PaperVO> voList = page.getRecords().stream().map(p -> {
            PaperVO vo = new PaperVO();
            BeanUtils.copyProperties(p, vo);
            vo.setIndexTypes(indexMap.getOrDefault(p.getId(), Collections.emptyList()));
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<PaperVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public PaperVO getById(Long id) {
        Paper entity = paperMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("论文不存在");
        }
        if ("TEACHER".equals(getCurrentUserRole()) && !entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权查看他人数据");
        }
        List<PaperIndex> indexes = paperIndexMapper.selectList(
                new LambdaQueryWrapper<PaperIndex>().eq(PaperIndex::getPaperId, id));
        PaperVO vo = new PaperVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setIndexTypes(indexes.stream().map(PaperIndex::getIndexType).collect(Collectors.toList()));
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    @Transactional
    public PaperVO create(PaperDTO dto) {
        checkDoiUnique(dto.getDoi(), null);

        Paper entity = new Paper();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(getCurrentUserId());
        paperMapper.insert(entity);

        saveIndexTypes(entity.getId(), dto.getIndexTypes());
        scoreCalculationService.clearMaxCache();
        return getById(entity.getId());
    }

    @Transactional
    public PaperVO update(Long id, PaperDTO dto) {
        Paper entity = paperMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("论文不存在");
        }
        if (!entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权修改他人数据");
        }
        checkDoiUnique(dto.getDoi(), id);

        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        paperMapper.updateById(entity);

        paperIndexMapper.delete(new LambdaQueryWrapper<PaperIndex>().eq(PaperIndex::getPaperId, id));
        saveIndexTypes(id, dto.getIndexTypes());
        scoreCalculationService.clearMaxCache();
        return getById(id);
    }

    @Transactional
    public void delete(Long id) {
        Paper entity = paperMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("论文不存在");
        }
        if (!entity.getUserId().equals(getCurrentUserId())) {
            throw new BusinessException("无权删除他人数据");
        }
        paperIndexMapper.delete(new LambdaQueryWrapper<PaperIndex>().eq(PaperIndex::getPaperId, id));
        paperMapper.deleteById(id);
        scoreCalculationService.clearMaxCache();
    }

    private void saveIndexTypes(Long paperId, List<String> indexTypes) {
        if (indexTypes != null && !indexTypes.isEmpty()) {
            List<PaperIndex> list = indexTypes.stream().map(type -> {
                PaperIndex pi = new PaperIndex();
                pi.setPaperId(paperId);
                pi.setIndexType(type);
                return pi;
            }).collect(Collectors.toList());
            paperIndexMapper.insert(list);
        }
    }

    private List<String> parseIndexTypes(String indexTypes) {
        if (indexTypes == null || indexTypes.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(indexTypes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private LambdaQueryWrapper<Paper> buildQueryWrapper(PaperQueryDTO query) {
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(Paper::getUserId, userId);
        }

        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            wrapper.like(Paper::getTitle, query.getTitle());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(Paper::getType, query.getType());
        }
        if (query.getJournalName() != null && !query.getJournalName().isEmpty()) {
            wrapper.like(Paper::getJournalName, query.getJournalName());
        }
        if (query.getAuthorOrder() != null) {
            wrapper.eq(Paper::getAuthorOrder, query.getAuthorOrder());
        }
        if (query.getPublishDateBegin() != null) {
            wrapper.ge(Paper::getPublishDate, query.getPublishDateBegin());
        }
        if (query.getPublishDateEnd() != null) {
            wrapper.le(Paper::getPublishDate, query.getPublishDateEnd());
        }

        wrapper.orderByDesc(Paper::getPublishDate);
        return wrapper;
    }

    private String getCurrentUserRole() {
        return (String) request.getAttribute("role");
    }

    private void checkDoiUnique(String doi, Long excludeId) {
        if (doi == null || doi.isEmpty()) {
            return;
        }
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<Paper>()
                .eq(Paper::getDoi, doi);
        if (excludeId != null) {
            wrapper.ne(Paper::getId, excludeId);
        }
        Long count = paperMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("DOI已存在");
        }
    }

    private Long getCurrentUserId() {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        return userId;
    }
}