package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.CompetitionDTO;
import com.portrait.dto.CompetitionQueryDTO;
import com.portrait.service.CompetitionService;
import com.portrait.vo.CompetitionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "竞赛指导管理")
@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    @Resource
    private CompetitionService competitionService;

    @Operation(summary = "分页查询竞赛列表")
    @GetMapping
    public Result<PageResult<CompetitionVO>> page(CompetitionQueryDTO query) {
        return Result.success(PageResult.of(competitionService.page(query)));
    }

    @Operation(summary = "获取竞赛详情")
    @GetMapping("/{id}")
    public Result<CompetitionVO> getById(@PathVariable Long id) {
        return Result.success(competitionService.getById(id));
    }

    @Operation(summary = "新增竞赛记录")
    @PostMapping
    public Result<CompetitionVO> create(@Valid @RequestBody CompetitionDTO dto) {
        return Result.success(competitionService.create(dto));
    }

    @Operation(summary = "编辑竞赛记录")
    @PutMapping("/{id}")
    public Result<CompetitionVO> update(@PathVariable Long id, @Valid @RequestBody CompetitionDTO dto) {
        return Result.success(competitionService.update(id, dto));
    }

    @Operation(summary = "删除竞赛记录")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        competitionService.delete(id);
        return Result.success();
    }
}