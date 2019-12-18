package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.utils.IdUtils;

@lombok.Data
@TableName("data")
public class DataTable {
    private String id;

    private String url;

    private String column1;

    private String column2;

    private String column3;

    private String column4;

    private String column5;

    private String column6;

    private String column7;

    private String column8;

    private String column9;

    private String column10;

    public DataTable(){
        setId(IdUtils.getRandomIdByUUID());
    }
}