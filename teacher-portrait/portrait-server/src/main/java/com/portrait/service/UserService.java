package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.dto.UserDTO;
import com.portrait.entity.User;
import com.portrait.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Page<User> page(Integer pageNum, Integer pageSize, String workNo, String name, String college) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .like(workNo != null && !workNo.isEmpty(), User::getWorkNo, workNo)
                .like(name != null && !name.isEmpty(), User::getName, name)
                .like(college != null && !college.isEmpty(), User::getCollege, college)
                .orderByAsc(User::getWorkNo);
        return userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) throw new BusinessException("用户不存在");
        user.setPassword(null);
        return user;
    }

    public User create(UserDTO dto) {
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getWorkNo, dto.getWorkNo()));
        if (count > 0) throw new BusinessException("工号已存在");

        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode("123456"));
        } else {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userMapper.insert(entity);
        entity.setPassword(null);
        return entity;
    }

    public User update(Long id, UserDTO dto) {
        User entity = userMapper.selectById(id);
        if (entity == null) throw new BusinessException("用户不存在");

        if (!entity.getWorkNo().equals(dto.getWorkNo())) {
            Long count = userMapper.selectCount(
                    new LambdaQueryWrapper<User>().eq(User::getWorkNo, dto.getWorkNo()));
            if (count > 0) throw new BusinessException("工号已存在");
        }

        entity.setName(dto.getName());
        entity.setCollege(dto.getCollege());
        entity.setRole(dto.getRole());
        userMapper.updateById(entity);
        entity.setPassword(null);
        return entity;
    }

    public void delete(Long id) {
        User entity = userMapper.selectById(id);
        if (entity == null) throw new BusinessException("用户不存在");

        Long currentUserId = getCurrentUserId();
        if (entity.getId().equals(currentUserId)) throw new BusinessException("不能删除自己");

        userMapper.deleteById(id);
    }

    public void resetPassword(Long id) {
        User entity = userMapper.selectById(id);
        if (entity == null) throw new BusinessException("用户不存在");
        entity.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(entity);
    }

    private Long getCurrentUserId() {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new BusinessException(401, "未登录");
        return userId;
    }
}