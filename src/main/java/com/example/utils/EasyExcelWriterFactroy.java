package com.example.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class EasyExcelWriterFactroy extends ExcelWriter {
    //输出流
    private OutputStream outputStream;
    //sheet的序号从1开始
    private int sheetNo = 1;

    public EasyExcelWriterFactroy(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public EasyExcelWriterFactroy write(List<? extends BaseRowModel> list, String sheetName, Class<? extends BaseRowModel> clazz) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, clazz);
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}