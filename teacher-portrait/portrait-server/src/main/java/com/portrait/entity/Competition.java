package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("competition")
public class Competition {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String organizer;
    private LocalDate competitionDate;
    private String studentTeam;
    private String awardLevel;
    private String awardGrade;
    private Integer guideRank;
    private String certificateNo;
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}