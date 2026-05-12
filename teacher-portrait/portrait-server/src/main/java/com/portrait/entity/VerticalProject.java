package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("vertical_project")
public class VerticalProject {
    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}