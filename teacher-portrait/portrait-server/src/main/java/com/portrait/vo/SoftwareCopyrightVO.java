package com.portrait.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SoftwareCopyrightVO {
    private Long id;
    private Long userId;
    private String name;
    private String registrationNo;
    private String version;
    private LocalDate devCompletionDate;
    private LocalDate firstPublishDate;
    private LocalDate registrationDate;
    private String copyrightOwners;
    private String remark;
    private String teacherName;
    private String teacherCollege;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}