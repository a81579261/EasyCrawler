package com.example.dto;

import com.example.entity.DataTable;
import lombok.Data;

@Data
public class PostRequestDto extends DataTable {
    private String page;
    private String content;
    private String jsonPathStr;
    /**
     * 递增参数
     */
    private String ascParam;
    private String maxAscParam;
}
