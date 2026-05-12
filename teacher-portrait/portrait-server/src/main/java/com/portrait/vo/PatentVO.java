package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatentVO {
    private Long id;
    private Long userId;
    private String name;
    private String type;
    private String applicationNo;
    private String grantNo;
    private LocalDate applicationDate;
    private LocalDate grantDate;
    private String status;
    private String inventors;
    private String patentee;
    private Integer isCounted;
    private String remark;
    private String teacherName;
    private String teacherCollege;
    private List<PatentTransferVO> transfers;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}