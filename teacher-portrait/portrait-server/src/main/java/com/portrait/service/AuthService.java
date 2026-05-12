package com.portrait.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.portrait.common.BusinessException;
import com.portrait.dto.LoginDTO;
import com.portrait.entity.User;
import com.portrait.mapper.UserMapper;
import com.portrait.util.JwtUtil;
import com.portrait.vo.LoginVO;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    @Resource
    private UserMapper userMapper;

    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getWorkNo, loginDTO.getWorkNo());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(401, "工号或密码错误");
        }

        String inputPasswordMd5 = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!inputPasswordMd5.equals(user.getPassword())) {
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