package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.PaperDTO;
import com.portrait.dto.PaperQueryDTO;
import com.portrait.service.PaperService;
import com.portrait.vo.PaperVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "论文管理")
@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Resource
    private PaperService paperService;

    @Operation(summary = "分页查询论文列表")
    @GetMapping
    public Result<PageResult<PaperVO>> page(PaperQueryDTO query) {
        return Result.success(PageResult.of(paperService.page(query)));
    }

    @Operation(summary = "获取论文详情")
    @GetMapping("/{id}")
    public Result<PaperVO> getById(@PathVariable Long id) {
        return Result.success(paperService.getById(id));
    }

    @Operation(summary = "新增论文")
    @PostMapping
    public Result<PaperVO> create(@Valid @RequestBody PaperDTO dto) {
        return Result.success(paperService.create(dto));
    }

    @Operation(summary = "编辑论文")
    @PutMapping("/{id}")
    public Result<PaperVO> update(@PathVariable Long id, @Valid @RequestBody PaperDTO dto) {
        return Result.success(paperService.update(id, dto));
    }

    @Operation(summary = "删除论文")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        paperService.delete(id);
        return Result.success();
    }
}