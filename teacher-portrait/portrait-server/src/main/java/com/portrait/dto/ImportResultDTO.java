package com.portrait.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResultDTO {
    private int successCount;
    private int skipCount;
    private int failCount;
    private List<String> skipReasons = new ArrayList<>();
    private List<String> failReasons = new ArrayList<>();
}