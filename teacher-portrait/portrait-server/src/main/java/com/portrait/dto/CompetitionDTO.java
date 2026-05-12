package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CompetitionDTO {
    @NotBlank(message = "竞赛名称不能为空")
    private String name;

    private String organizer;

    @NotNull(message = "参赛时间不能为空")
    private LocalDate competitionDate;

    private String studentTeam;

    @NotBlank(message = "获奖级别不能为空")
    private String awardLevel;

    @NotBlank(message = "获奖等级不能为空")
    private String awardGrade;

    private Integer guideRank;
    private String certificateNo;
    private String remark;
}