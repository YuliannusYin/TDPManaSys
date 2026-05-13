package com.portrait.controller;

import com.portrait.common.BusinessException;
import com.portrait.common.Result;
import com.portrait.entity.ScoreConfig;
import com.portrait.mapper.ScoreConfigMapper;
import com.portrait.service.ScoreCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Tag(name = "评分权重配置")
@RestController
@RequestMapping("/api/score-config")
public class ScoreConfigController {

    @Resource
    private ScoreConfigMapper scoreConfigMapper;

    @Resource
    private ScoreCalculationService scoreCalculationService;

    @Resource
    private HttpServletRequest request;

    @Operation(summary = "获取当前权重配置")
    @GetMapping
    public Result<List<ScoreConfig>> get() {
        return Result.success(scoreConfigMapper.selectList(null));
    }

    @Operation(summary = "更新权重配置")
    @PutMapping
    public Result<?> update(@RequestBody List<Map<String, Object>> configs) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> c : configs) {
            Object w = c.get("weight");
            BigDecimal weight = w instanceof BigDecimal ? (BigDecimal) w : new BigDecimal(w.toString());
            total = total.add(weight);
        }
        total = total.setScale(2, RoundingMode.HALF_UP);
        if (total.compareTo(new BigDecimal("100.00")) != 0) {
            throw new BusinessException("权重总和必须为 100%，当前为 " + total + "%");
        }

        for (Map<String, Object> c : configs) {
            Object idObj = c.get("id");
            if (idObj == null) continue;
            Long id = Long.valueOf(idObj.toString());
            ScoreConfig config = scoreConfigMapper.selectById(id);
            if (config != null) {
                Object w = c.get("weight");
                config.setWeight(w instanceof BigDecimal ? (BigDecimal) w : new BigDecimal(w.toString()));
                if (c.containsKey("scoringRules")) {
                    config.setScoringRules((String) c.get("scoringRules"));
                }
                scoreConfigMapper.updateById(config);
            }
        }

        scoreCalculationService.clearMaxCache();
        return Result.success();
    }
}