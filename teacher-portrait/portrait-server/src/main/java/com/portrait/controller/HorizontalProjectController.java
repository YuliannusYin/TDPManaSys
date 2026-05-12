package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.HorizontalProjectDTO;
import com.portrait.dto.HorizontalProjectQueryDTO;
import com.portrait.service.HorizontalProjectService;
import com.portrait.vo.HorizontalProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "横向项目管理")
@RestController
@RequestMapping("/api/horizontal-projects")
public class HorizontalProjectController {

    @Resource
    private HorizontalProjectService horizontalProjectService;

    @Operation(summary = "分页查询横向项目列表")
    @GetMapping
    public Result<PageResult<HorizontalProjectVO>> page(HorizontalProjectQueryDTO query) {
        return Result.success(PageResult.of(horizontalProjectService.page(query)));
    }

    @Operation(summary = "获取横向项目详情")
    @GetMapping("/{id}")
    public Result<HorizontalProjectVO> getById(@PathVariable Long id) {
        return Result.success(horizontalProjectService.getById(id));
    }

    @Operation(summary = "新增横向项目")
    @PostMapping
    public Result<HorizontalProjectVO> create(@Valid @RequestBody HorizontalProjectDTO dto) {
        return Result.success(horizontalProjectService.create(dto));
    }

    @Operation(summary = "编辑横向项目")
    @PutMapping("/{id}")
    public Result<HorizontalProjectVO> update(@PathVariable Long id, @Valid @RequestBody HorizontalProjectDTO dto) {
        return Result.success(horizontalProjectService.update(id, dto));
    }

    @Operation(summary = "删除横向项目")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        horizontalProjectService.delete(id);
        return Result.success();
    }
}