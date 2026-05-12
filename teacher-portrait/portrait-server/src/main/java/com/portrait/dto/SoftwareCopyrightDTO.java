package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SoftwareCopyrightDTO {
    @NotBlank(message = "软件名称不能为空")
    private String name;

    @NotBlank(message = "登记号不能为空")
    private String registrationNo;

    private String version;

    @NotNull(message = "开发完成日期不能为空")
    private LocalDate devCompletionDate;

    private LocalDate firstPublishDate;

    @NotNull(message = "登记日期不能为空")
    private LocalDate registrationDate;

    private String copyrightOwners;
    private String remark;
}