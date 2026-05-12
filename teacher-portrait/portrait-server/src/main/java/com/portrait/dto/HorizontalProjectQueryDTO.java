package com.portrait.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HorizontalProjectQueryDTO {
    private String name;
    private String companyName;
    private String status;
    private String role;
    private LocalDate signDateBegin;
    private LocalDate signDateEnd;
    private BigDecimal amountMin;
    private BigDecimal amountMax;
    private Integer page = 1;
    private Integer size = 10;
}