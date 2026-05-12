package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.SoftwareCopyrightDTO;
import com.portrait.dto.SoftwareCopyrightQueryDTO;
import com.portrait.service.SoftwareCopyrightService;
import com.portrait.vo.SoftwareCopyrightVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "软著管理")
@RestController
@RequestMapping("/api/software-copyrights")
public class SoftwareCopyrightController {

    @Resource
    private SoftwareCopyrightService softwareCopyrightService;

    @Operation(summary = "分页查询软著列表")
    @GetMapping
    public Result<PageResult<SoftwareCopyrightVO>> page(SoftwareCopyrightQueryDTO query) {
        return Result.success(PageResult.of(softwareCopyrightService.page(query)));
    }

    @Operation(summary = "获取软著详情")
    @GetMapping("/{id}")
    public Result<SoftwareCopyrightVO> getById(@PathVariable Long id) {
        return Result.success(softwareCopyrightService.getById(id));
    }

    @Operation(summary = "新增软著")
    @PostMapping
    public Result<SoftwareCopyrightVO> create(@Valid @RequestBody SoftwareCopyrightDTO dto) {
        return Result.success(softwareCopyrightService.create(dto));
    }

    @Operation(summary = "编辑软著")
    @PutMapping("/{id}")
    public Result<SoftwareCopyrightVO> update(@PathVariable Long id, @Valid @RequestBody SoftwareCopyrightDTO dto) {
        return Result.success(softwareCopyrightService.update(id, dto));
    }

    @Operation(summary = "删除软著")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        softwareCopyrightService.delete(id);
        return Result.success();
    }
}