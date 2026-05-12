package com.portrait.controller;

import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.PatentDTO;
import com.portrait.dto.PatentQueryDTO;
import com.portrait.dto.PatentTransferDTO;
import com.portrait.service.PatentService;
import com.portrait.vo.PatentTransferVO;
import com.portrait.vo.PatentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "专利管理")
@RestController
@RequestMapping("/api/patents")
public class PatentController {

    @Resource
    private PatentService patentService;

    @Operation(summary = "分页查询专利列表")
    @GetMapping
    public Result<PageResult<PatentVO>> page(PatentQueryDTO query) {
        return Result.success(PageResult.of(patentService.page(query)));
    }

    @Operation(summary = "获取专利详情")
    @GetMapping("/{id}")
    public Result<PatentVO> getById(@PathVariable Long id) {
        return Result.success(patentService.getById(id));
    }

    @Operation(summary = "新增专利")
    @PostMapping
    public Result<PatentVO> create(@Valid @RequestBody PatentDTO dto) {
        return Result.success(patentService.create(dto));
    }

    @Operation(summary = "编辑专利")
    @PutMapping("/{id}")
    public Result<PatentVO> update(@PathVariable Long id, @Valid @RequestBody PatentDTO dto) {
        return Result.success(patentService.update(id, dto));
    }

    @Operation(summary = "删除专利")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        patentService.delete(id);
        return Result.success();
    }

    @Operation(summary = "专利转让")
    @PostMapping("/{id}/transfer")
    public Result<PatentTransferVO> transfer(@PathVariable Long id, @Valid @RequestBody PatentTransferDTO dto) {
        return Result.success(patentService.transfer(id, dto));
    }

    @Operation(summary = "获取专利转让记录")
    @GetMapping("/{id}/transfers")
    public Result<List<PatentTransferVO>> getTransfers(@PathVariable Long id) {
        return Result.success(patentService.getTransfers(id));
    }
}