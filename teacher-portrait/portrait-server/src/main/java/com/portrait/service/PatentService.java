package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.PatentDTO;
import com.portrait.dto.PatentQueryDTO;
import com.portrait.dto.PatentTransferDTO;
import com.portrait.entity.Patent;
import com.portrait.entity.PatentTransfer;
import com.portrait.entity.User;
import com.portrait.mapper.PatentMapper;
import com.portrait.mapper.PatentTransferMapper;
import com.portrait.mapper.UserMapper;
import com.portrait.vo.PatentTransferVO;
import com.portrait.vo.PatentVO;
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
public class PatentService {

    @Resource
    private PatentMapper patentMapper;

    @Resource
    private PatentTransferMapper patentTransferMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    public Page<PatentVO> page(PatentQueryDTO query) {
        LambdaQueryWrapper<Patent> wrapper = buildQueryWrapper(query);
        Page<Patent> page = patentMapper.selectPage(
                new Page<>(query.getPage(), query.getSize()), wrapper);

        List<Long> userIds = page.getRecords().stream()
                .map(Patent::getUserId).distinct().collect(Collectors.toList());

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap()
                : userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, Function.identity()));

        List<PatentVO> voList = page.getRecords().stream().map(p -> {
            PatentVO vo = new PatentVO();
            BeanUtils.copyProperties(p, vo);
            User user = userMap.get(p.getUserId());
            if (user != null) {
                vo.setTeacherName(user.getName());
                vo.setTeacherCollege(user.getCollege());
            }
            return vo;
        }).collect(Collectors.toList());

        Page<PatentVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    public PatentVO getById(Long id) {
        Patent entity = patentMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("专利不存在");
        }
        return toVO(entity);
    }

    public PatentVO create(PatentDTO dto) {
        Patent entity = new Patent();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getIsCounted() == null) {
            entity.setIsCounted(1);
        }
        entity.setUserId(getCurrentUserId());
        patentMapper.insert(entity);
        return getById(entity.getId());
    }

    public PatentVO update(Long id, PatentDTO dto) {
        Patent entity = patentMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("专利不存在");
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setId(id);
        entity.setUserId(getCurrentUserId());
        patentMapper.updateById(entity);
        return getById(id);
    }

    public void delete(Long id) {
        Patent entity = patentMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("专利不存在");
        }
        patentMapper.deleteById(id);
    }

    @Transactional
    public PatentTransferVO transfer(Long patentId, PatentTransferDTO dto) {
        Patent patent = patentMapper.selectById(patentId);
        if (patent == null) {
            throw new BusinessException("专利不存在");
        }
        if (!"已授权".equals(patent.getStatus())) {
            throw new BusinessException("仅已授权专利可以转让");
        }

        PatentTransfer transfer = new PatentTransfer();
        transfer.setPatentId(patentId);
        BeanUtils.copyProperties(dto, transfer);
        patentTransferMapper.insert(transfer);

        if (dto.getIsCounted() != null) {
            patent.setIsCounted(dto.getIsCounted());
            patentMapper.updateById(patent);
        }

        PatentTransferVO vo = new PatentTransferVO();
        BeanUtils.copyProperties(transfer, vo);
        return vo;
    }

    public List<PatentTransferVO> getTransfers(Long patentId) {
        List<PatentTransfer> transfers = patentTransferMapper.selectList(
                new LambdaQueryWrapper<PatentTransfer>()
                        .eq(PatentTransfer::getPatentId, patentId)
                        .orderByDesc(PatentTransfer::getTransferDate));
        return transfers.stream().map(t -> {
            PatentTransferVO vo = new PatentTransferVO();
            BeanUtils.copyProperties(t, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    private PatentVO toVO(Patent entity) {
        PatentVO vo = new PatentVO();
        BeanUtils.copyProperties(entity, vo);
        User user = userMapper.selectById(entity.getUserId());
        if (user != null) {
            vo.setTeacherName(user.getName());
            vo.setTeacherCollege(user.getCollege());
        }
        return vo;
    }

    private LambdaQueryWrapper<Patent> buildQueryWrapper(PatentQueryDTO query) {
        LambdaQueryWrapper<Patent> wrapper = new LambdaQueryWrapper<>();

        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if ("TEACHER".equals(role)) {
            wrapper.eq(Patent::getUserId, userId);
        }

        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(Patent::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(Patent::getType, query.getType());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            wrapper.eq(Patent::getStatus, query.getStatus());
        }
        if (query.getApplicationDateBegin() != null) {
            wrapper.ge(Patent::getApplicationDate, query.getApplicationDateBegin());
        }
        if (query.getApplicationDateEnd() != null) {
            wrapper.le(Patent::getApplicationDate, query.getApplicationDateEnd());
        }

        wrapper.orderByDesc(Patent::getApplicationDate);
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