package com.portrait.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class PaperDTO {
    @NotBlank(message = "论文题目不能为空")
    private String title;

    @NotBlank(message = "论文类型不能为空")
    private String type;

    @NotBlank(message = "期刊/会议名称不能为空")
    private String journalName;

    private String volume;
    private String issue;
    private String pages;

    @NotNull(message = "发表时间不能为空")
    private LocalDate publishDate;

    private String authors;

    @NotNull(message = "作者排序不能为空")
    private Integer authorOrder;

    private String doi;
    private String remark;

    private List<String> indexTypes;
}