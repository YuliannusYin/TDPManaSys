package com.portrait.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VerticalProjectQueryDTO {
    private String name;
    private String projectNo;
    private String level;
    private String status;
    private String role;
    private LocalDate startDateBegin;
    private LocalDate startDateEnd;
    private BigDecimal fundingMin;
    private BigDecimal fundingMax;
    private Integer page = 1;
    private Integer size = 10;
}