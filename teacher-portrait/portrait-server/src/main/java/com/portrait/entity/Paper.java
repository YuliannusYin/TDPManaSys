package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("paper")
public class Paper {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String type;
    private String journalName;
    private String volume;
    private String issue;
    private String pages;
    private LocalDate publishDate;
    private String authors;
    private Integer authorOrder;
    private String doi;
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}