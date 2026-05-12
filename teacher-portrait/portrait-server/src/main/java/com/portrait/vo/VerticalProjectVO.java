package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VerticalProjectVO {
    private Long id;
    private Long userId;
    private String name;
    private String projectNo;
    private String level;
    private String sourceUnit;
    private LocalDate startDate;
    private LocalDate plannedEndDate;
    private BigDecimal funding;
    private String role;
    private String status;
    private String remark;
    private String teacherName;
    private String teacherCollege;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}