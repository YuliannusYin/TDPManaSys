package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VerticalProjectDTO {
    @NotBlank(message = "项目名称不能为空")
    private String name;

    private String projectNo;

    @NotBlank(message = "项目级别不能为空")
    private String level;

    private String sourceUnit;

    @NotNull(message = "立项时间不能为空")
    private LocalDate startDate;

    private LocalDate plannedEndDate;
    private BigDecimal funding;

    @NotBlank(message = "参与角色不能为空")
    private String role;

    @NotBlank(message = "项目状态不能为空")
    private String status;

    private String remark;
}