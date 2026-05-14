package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class PortraitDashboardVO {
    private Long projectTotalCount;
    private BigDecimal totalFunding;
    private Long paperTotalCount;
    private Long paperACount;
    private Long paperBCount;
    private Long patentGrantedCount;
    private Long softwareCount;
    private Long competitionAwardCount;
    private Map<String, BigDecimal> rawScores;
    private Map<String, BigDecimal> normalizedScores;
}