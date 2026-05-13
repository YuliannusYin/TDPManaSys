package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class PortraitTrendVO {
    private String year;
    private Long projectCount;
    private BigDecimal projectFunding;
    private Long patentCount;
    private Long softwareCount;
    private Long paperCount;
    private Long competitionCount;
}