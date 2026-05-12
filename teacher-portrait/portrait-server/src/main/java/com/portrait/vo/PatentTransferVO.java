package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PatentTransferVO {
    private Long id;
    private Long patentId;
    private LocalDate transferDate;
    private String transferee;
    private BigDecimal amount;
    private LocalDateTime createTime;
}