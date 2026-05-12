package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("horizontal_project")
public class HorizontalProject {
    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}