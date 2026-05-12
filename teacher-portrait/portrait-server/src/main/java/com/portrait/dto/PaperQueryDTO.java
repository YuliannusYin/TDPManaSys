package com.portrait.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaperQueryDTO {
    private String title;
    private String type;
    private String journalName;
    private Integer authorOrder;
    private LocalDate publishDateBegin;
    private LocalDate publishDateEnd;
    private String indexTypes;
    private Integer page = 1;
    private Integer size = 10;
}