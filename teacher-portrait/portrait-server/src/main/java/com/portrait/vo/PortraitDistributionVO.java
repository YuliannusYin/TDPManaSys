package com.portrait.vo;

import lombok.Data;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class PortraitDistributionVO {
    private List<Map<String, Object>> projectLevel = new ArrayList<>();
    private List<Map<String, Object>> patentType = new ArrayList<>();
    private List<Map<String, Object>> paperClass = new ArrayList<>();
    private List<Map<String, Object>> competitionLevel = new ArrayList<>();
}