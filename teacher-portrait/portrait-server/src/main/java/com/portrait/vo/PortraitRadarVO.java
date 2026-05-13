package com.portrait.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class PortraitRadarVO {
    private Long userId;
    private String userName;
    private String college;
    private Map<String, BigDecimal> rawScores;
    private Map<String, BigDecimal> normalizedScores;
}