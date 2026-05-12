package com.portrait.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SoftwareCopyrightQueryDTO {
    private String name;
    private LocalDate registrationDateBegin;
    private LocalDate registrationDateEnd;
    private Integer page = 1;
    private Integer size = 10;
}