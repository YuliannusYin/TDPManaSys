package com.portrait.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("paper_index")
public class PaperIndex {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long paperId;
    private String indexType;
}