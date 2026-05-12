package com.portrait.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaperVO {
    private Long id;
    private Long userId;
    private String title;
    private String type;
    private String journalName;
    private String volume;
    private String issue;
    private String pages;
    private LocalDate publishDate;
    private String authors;
    private Integer authorOrder;
    private String doi;
    private String remark;
    private List<String> indexTypes;
    private String teacherName;
    private String teacherCollege;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}