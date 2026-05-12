package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HorizontalProjectVO {
    private Long id;
    private Long userId;
    private String name;
    private String companyName;
    private BigDecimal contractAmount;
    private LocalDate signDate;
    private LocalDate endDate;
    private String role;
    private String status;
    private String remark;
    private String teacherName;
    private String teacherCollege;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}