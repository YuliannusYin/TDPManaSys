package com.portrait.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CompetitionQueryDTO {
    private String name;
    private String awardLevel;
    private String awardGrade;
    private LocalDate competitionDateBegin;
    private LocalDate competitionDateEnd;
    private Integer page = 1;
    private Integer size = 10;
}