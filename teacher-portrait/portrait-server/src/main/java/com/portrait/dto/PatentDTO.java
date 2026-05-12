package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PatentDTO {
    @NotBlank(message = "专利名称不能为空")
    private String name;

    @NotBlank(message = "专利类型不能为空")
    private String type;

    private String applicationNo;
    private String grantNo;

    @NotNull(message = "申请日期不能为空")
    private LocalDate applicationDate;

    private LocalDate grantDate;

    @NotBlank(message = "专利状态不能为空")
    private String status;

    private String inventors;
    private String patentee;
    private Integer isCounted;
    private String remark;
}