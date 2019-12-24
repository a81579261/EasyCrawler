package com.example.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.utils.IdUtils;
import com.example.utils.easyexcel.EasyBaseModel;

@lombok.Data
@TableName("data")
public class DataTable implements EasyBaseModel {
    @ExcelIgnore
    private String id;
    @ExcelIgnore
    private String url;
    @ExcelProperty(value = "字段1",index = 1)
    private String column1;
    @ExcelProperty(value = "字段2",index = 2)
    private String column2;
    @ExcelProperty(value = "字段3",index = 3)
    private String column3;
    @ExcelProperty(value = "字段4",index = 4)
    private String column4;
    @ExcelProperty(value = "字段5",index = 5)
    private String column5;
    @ExcelProperty(value = "字段6",index = 6)
    private String column6;
    @ExcelProperty(value = "字段7",index = 7)
    private String column7;
    @ExcelProperty(value = "字段8",index = 8)
    private String column8;
    @ExcelProperty(value = "字段9",index = 9)
    private String column9;
    @ExcelProperty(value = "字段10",index = 10)
    private String column10;
    @ExcelIgnore
    private String exportKey;

    public DataTable(){
        setId(IdUtils.getRandomIdByUUID());
    }
}