package com.example.service;

import com.example.entity.DataTable;
import com.example.mapper.DataMapper;
import com.example.utils.easyexcel.EasyExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {


    @Autowired
    private DataMapper dataMapper;

    public void exportExcel(String key) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("export_Key", key);
        List<DataTable> dataTables = dataMapper.selectByMap(map);
        if (dataTables.size() == 0) {
            throw new Exception("导出Excel为空");
        }
        //获取当前桌面地址
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com = fsv.getHomeDirectory();
        //时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String excelFilePath = com.getPath() + "/新建XLSX工作表" + sdf.format(new Date()) + ".xlsx";
        EasyExcelUtil.simpleWrite(excelFilePath, "Sheet1", null, dataTables, DataTable.class);
    }

}
