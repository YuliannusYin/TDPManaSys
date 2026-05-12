package com.portrait.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatentQueryDTO {
    private String name;
    private String type;
    private String status;
    private LocalDate applicationDateBegin;
    private LocalDate applicationDateEnd;
    private Integer page = 1;
    private Integer size = 10;
}