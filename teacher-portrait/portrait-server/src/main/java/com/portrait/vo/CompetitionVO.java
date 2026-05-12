package com.portrait.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CompetitionVO {
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
    private String teacherName;
    private String teacherCollege;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}