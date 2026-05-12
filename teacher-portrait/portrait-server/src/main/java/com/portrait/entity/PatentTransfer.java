package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("patent_transfer")
public class PatentTransfer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long patentId;
    private LocalDate transferDate;
    private String transferee;
    private BigDecimal amount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}