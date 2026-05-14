package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.portrait.common.BusinessException;
import com.portrait.dto.LoginDTO;
import com.portrait.entity.User;
import com.portrait.mapper.UserMapper;
import com.portrait.util.JwtUtil;
import com.portrait.vo.LoginVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthService {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getWorkNo, loginDTO.getWorkNo());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(401, "工号或密码错误");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "工号或密码错误");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getWorkNo(), user.getRole(), user.getName());

        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .workNo(user.getWorkNo())
                .name(user.getName())
                .college(user.getCollege())
                .role(user.getRole())
                .build();
    }
}