package com.example.utils;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public final class EasyExcelUtil {

    //log日志
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelUtil.class);

    private EasyExcelUtil() {

    }

    /**
     * 模型解析监听器
     */
    private static class ModelExcelListener extends AnalysisEventListener {

        //自定义用于暂时存储data
        //可以通过实例获取该值
        private List<Object> datas = new ArrayList<>();

        /**
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         * @param object
         * @param context
         */
        @Override
        public void invoke(Object object, AnalysisContext context) {
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            datas.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // Do nothing because of X and Y.
        }

        public List<Object> getDatas() {
            return datas;
        }

        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }

    /**
     * 使用模型来读取Excel（一个 sheet）
     * @param inputStream Excel的输入流
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return 返回模型的列表(为object列表, 需强转)
     */
    public static <T extends BaseRowModel> List<T> readExcelWithModel(InputStream inputStream, Class<T> clazz,
                                                                      ExcelTypeEnum excelTypeEnum) {
        // 解析每行结果在listener中处理
        ModelExcelListener listener = new ModelExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        if (excelReader == null) {
            return new ArrayList<>();
        }
        //默认只有一列表头
        excelReader.read(new Sheet(1, 1, clazz));
        return BeanConvert.objectConvertBean(listener.getDatas(), clazz);
    }

    /**
     * 使用模型来读取Excel（多个 sheet）
     * @param inputStream Excel的输入流
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @return 返回模型的列表(为object列表, 需强转)
     */
    public static <T extends BaseRowModel> List<T> readExcelWithModelSheet(InputStream inputStream, Class<T> clazz,
                                                                           ExcelTypeEnum excelTypeEnum) {
        // 解析每行结果在listener中处理
        ModelExcelListener listener = new ModelExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream, excelTypeEnum, null, listener);
        if (excelReader == null) {
            return new ArrayList<>();
        }
        for (Sheet sheet : excelReader.getSheets()) {
            if (clazz != null) {
                sheet.setClazz(clazz);
            }
            excelReader.read(sheet);
        }
        return BeanConvert.objectConvertBean(listener.getDatas(), clazz);
    }

    /**
     * 使用模型来写入Excel（一个 sheet）
     * 使用easyExcel导出出现的问题：
     * 1.导出的Excel中的数据是以.zip 结尾的一个文件
     * 原因：response设置必须写在writer输出流关闭之前
     * 2.导出无法打开xlsx文件：
     * 原因：设置响应头类型类型错误
     * 导出xls格式：
     * response.setContentType("application/vnd.ms-excel;charset=GBK");
     * 导出xlsx格式：
     * response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
     * 3.导出的excel为空
     * 原因： writer.finish()写在了out.flush之前，必须在最后关闭
     * 4.poi的jar版本过低问题
     * 原因：使用easyexcel大数量导出时，需要依赖org.apache.poi的3.17版本的jar而poi3.7与3.8/3.9
     * 版本之间方法变动比较大会导致easyexcel不能正常使用，所以需要确保poi版本为3.17,若用其他版本则导出报错
     * @param response Excel的输出流
     * @param data 要写入的以 模型 为单位的数据
     * @param fileName 文件名
     * @param sheetName 配置Excel的sheet名称
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @param clazz 模型的类
     */
    public static <T extends BaseRowModel> void writeExcelWithModel(HttpServletResponse response, List<T> data,
                                                                    String fileName, String sheetName, Class<T> clazz, ExcelTypeEnum excelTypeEnum) {
        try {
            OutputStream out = getOutputStream(response, fileName, excelTypeEnum);
            //这里指定需要表头，因为model通常包含表头信息
            ExcelWriter writer = new ExcelWriter(out, excelTypeEnum, true);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0, clazz);
            //设置自适应宽度
            sheet1.setAutoWidth(Boolean.TRUE);
            //设置表格样式
            sheet1.setTableStyle(createTableStyle());
            //设置sheetName
            sheet1.setSheetName(sheetName);
            //写数据
            writer.write(data, sheet1);
            //关闭writer的输出流
            writer.finish();
        } catch (Exception e) {
            LOGGER.error("EasyexcelUtil-->writeExcelWithModel-->Exception:", e);
        }
    }

    /**
     * 方法一：Map存储数据
     * 使用 模型 来写入Excel（多个 sheet）
     * @param response 响应
     * @param dataMap 封装数据：key=sheet名称唯一 value=list数据集合
     * @param fileName 文件名
     * @param excelTypeEnum excel类型
     */
    public static <T extends BaseRowModel> void writeExcelWithModelSheet(HttpServletResponse response,
                                                                         Map<String, List<T>> dataMap, String fileName, ExcelTypeEnum excelTypeEnum) {
        try {
            OutputStream out = getOutputStream(response, fileName, excelTypeEnum);
            //这里指定需要表头，因为model通常包含表头信息
            ExcelWriter writer = new ExcelWriter(out, excelTypeEnum, true);
            //循环写多个sheet
            int sheetNum = 1;
            for (Map.Entry<String, List<T>> map : dataMap.entrySet()) {
                //循环写sheet 数据全是List<String> 无模型映射关系
                Sheet sheet = new Sheet(sheetNum, 0, map.getValue().get(0).getClass());
                //设置自适应宽度
                sheet.setAutoWidth(Boolean.TRUE);
                //设置表格样式
                sheet.setTableStyle(createTableStyle());
                //设置sheetName
                sheet.setSheetName(map.getKey());
                //写数据
                writer.write(map.getValue(), sheet);
                sheetNum++;
            }
            //关闭writer的输出流
            writer.finish();
        } catch (Exception e) {
            LOGGER.error("EasyExcelUtil-->writeExcelWithModelSheet-->Exception", e);
        }
    }

    /**
     * 方法二：工厂方法
     * 使用模型来写入Excel（多个 sheet）
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz    映射实体类，Excel 模型
     */
    public static <T extends BaseRowModel> EasyExcelWriterFactroy writeExcelWithSheets(HttpServletResponse response,
                                                                                       List<T> list, String fileName, String sheetName, Class<T> clazz, ExcelTypeEnum excelTypeEnum) {
        OutputStream out = getOutputStream(response, fileName, excelTypeEnum);
        EasyExcelWriterFactroy writer = new EasyExcelWriterFactroy(out, excelTypeEnum);
        Sheet sheet = new Sheet(1, 0, clazz);
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        return writer;
    }

    /**
     * 得到流
     * @param response 响应
     * @param fileName 文件名
     * @param excelTypeEnum excel类型
     * @return
     */
    private static OutputStream getOutputStream(HttpServletResponse response, String fileName,
                                                ExcelTypeEnum excelTypeEnum) {
        try {
            // 设置响应输出的头类型
            if (Objects.equals(".xls", excelTypeEnum.getValue())) {
                //导出xls格式
                response.setContentType("application/vnd.ms-excel;charset=GBK");
            } else if (Objects.equals(".xlsx", excelTypeEnum.getValue())) {
                //导出xlsx格式
                response.setContentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=GBK");
            }
            // 设置下载文件名称(注意中文乱码)
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName).getBytes("GB2312"), "ISO8859-1") + excelTypeEnum
                            .getValue());
            return response.getOutputStream();
        } catch (IOException e) {
            LOGGER.error("EasyExcelUtil-->getOutputStream exception:", e);
        }
        return null;
    }

    /**
     * 设置表格样式
     * @return
     */
    public static TableStyle createTableStyle() {
        TableStyle tableStyle = new TableStyle();
        Font headFont = new Font();
        headFont.setBold(true);
        headFont.setFontHeightInPoints((short) 20);
        headFont.setFontName("楷体");
        tableStyle.setTableHeadFont(headFont);
        tableStyle.setTableHeadBackGroundColor(IndexedColors.LIGHT_GREEN);

        Font contentFont = new Font();
        contentFont.setFontHeightInPoints((short) 12);
        contentFont.setFontName("黑体");
        tableStyle.setTableContentFont(contentFont);
        return tableStyle;
    }


}