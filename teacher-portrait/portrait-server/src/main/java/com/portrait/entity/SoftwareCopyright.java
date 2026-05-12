package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("software_copyright")
public class SoftwareCopyright {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String registrationNo;
    private String version;
    private LocalDate devCompletionDate;
    private LocalDate firstPublishDate;
    private LocalDate registrationDate;
    private String copyrightOwners;
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}