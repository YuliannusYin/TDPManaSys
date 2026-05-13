package com.portrait.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portrait.common.BusinessException;
import com.portrait.common.PageResult;
import com.portrait.common.Result;
import com.portrait.dto.UserDTO;
import com.portrait.entity.User;
import com.portrait.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private HttpServletRequest request;

    @Operation(summary = "用户列表（分页+搜索）")
    @GetMapping
    public Result<PageResult<User>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String workNo,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }
        Page<User> p = userService.page(page, size, workNo, name, college);
        return Result.success(PageResult.of(p));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<User> create(@Valid @RequestBody UserDTO dto) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }
        return Result.success(userService.create(dto));
    }

    @Operation(summary = "编辑用户")
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }
        return Result.success(userService.update(id, dto));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }
        userService.delete(id);
        return Result.success();
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            throw new BusinessException(403, "仅管理员可操作");
        }
        userService.resetPassword(id);
        return Result.success();
    }
}