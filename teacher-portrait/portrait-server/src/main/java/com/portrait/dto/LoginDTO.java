package com.portrait.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "工号不能为空")
    private String workNo;

    @NotBlank(message = "密码不能为空")
    private String password;
}