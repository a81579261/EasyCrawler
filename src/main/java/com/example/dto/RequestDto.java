package com.example.dto;

import com.example.entity.DataTable;
import lombok.Data;

@Data
public class RequestDto extends DataTable {
    String page;
    String links1;
    String links2;
    String links3;
    String links4;
    int depth;
}
