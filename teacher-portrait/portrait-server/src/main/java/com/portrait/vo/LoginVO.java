package com.portrait.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private Long userId;
    private String workNo;
    private String name;
    private String college;
    private String role;
}