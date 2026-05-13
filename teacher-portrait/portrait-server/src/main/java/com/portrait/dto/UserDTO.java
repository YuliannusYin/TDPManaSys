package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank(message = "工号不能为空")
    private String workNo;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String college;

    @NotBlank(message = "角色不能为空")
    private String role;

    private String password;
}