package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.VerticalProjectDTO;
import com.portrait.dto.VerticalProjectQueryDTO;
import com.portrait.service.VerticalProjectService;
import com.portrait.vo.VerticalProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "纵向项目管理")
@RestController
@RequestMapping("/api/vertical-projects")
public class VerticalProjectController {

    @Resource
    private VerticalProjectService verticalProjectService;

    @Operation(summary = "分页查询纵向项目列表")
    @GetMapping
    public Result<PageResult<VerticalProjectVO>> page(VerticalProjectQueryDTO query) {
        return Result.success(PageResult.of(verticalProjectService.page(query)));
    }

    @Operation(summary = "获取纵向项目详情")
    @GetMapping("/{id}")
    public Result<VerticalProjectVO> getById(@PathVariable Long id) {
        return Result.success(verticalProjectService.getById(id));
    }

    @Operation(summary = "新增纵向项目")
    @PostMapping
    public Result<VerticalProjectVO> create(@Valid @RequestBody VerticalProjectDTO dto) {
        return Result.success(verticalProjectService.create(dto));
    }

    @Operation(summary = "编辑纵向项目")
    @PutMapping("/{id}")
    public Result<VerticalProjectVO> update(@PathVariable Long id, @Valid @RequestBody VerticalProjectDTO dto) {
        return Result.success(verticalProjectService.update(id, dto));
    }

    @Operation(summary = "删除纵向项目")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        verticalProjectService.delete(id);
        return Result.success();
    }
}