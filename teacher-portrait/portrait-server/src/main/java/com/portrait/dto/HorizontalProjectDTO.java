package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HorizontalProjectDTO {
    @NotBlank(message = "项目名称不能为空")
    private String name;

    @NotBlank(message = "合作企业名称不能为空")
    private String companyName;

    @NotNull(message = "合同金额不能为空")
    @DecimalMin(value = "0.01", message = "合同金额必须大于0")
    private BigDecimal contractAmount;

    @NotNull(message = "签订日期不能为空")
    private LocalDate signDate;

    private LocalDate endDate;

    @NotBlank(message = "参与角色不能为空")
    private String role;

    @NotBlank(message = "项目状态不能为空")
    private String status;

    private String remark;
}