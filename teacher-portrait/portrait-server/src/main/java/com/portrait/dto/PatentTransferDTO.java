package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PatentTransferDTO {
    @NotNull(message = "转让日期不能为空")
    private LocalDate transferDate;

    @NotBlank(message = "受让方名称不能为空")
    private String transferee;

    private BigDecimal amount;
}