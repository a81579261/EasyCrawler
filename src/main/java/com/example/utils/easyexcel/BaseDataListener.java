package com.example.utils.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
// 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
public abstract class BaseDataListener<T> extends AnalysisEventListener<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private  int BATCH_COUNT = 5;
    /**
     * 存在excle中每行数据
     */
    List<T> dataList = new ArrayList<T>(1024);
   
    public BaseDataListener() {
       
    }
    /**
     * 每batchCount条 清空list
     * @param batchCount 
     */
    public BaseDataListener(int batchCount) {
    	this.BATCH_COUNT = batchCount;
    }
    
    
    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        LOGGER.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            LOGGER.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        LOGGER.info("解析到一条头数据:{}", JSON.toJSONString(headMap));
    }
    
    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        dataList.add(data);
        // 达到BATCH_COUNT了，需要去处理一下已经存在的数据，防止数据几万条数据在内存，容易OOM
        if (dataList.size() >= BATCH_COUNT) {
            dealwithData();
            //处理完成清理 dataList
            dataList.clear();
        }
    } 
    /**
     * 所有数据解析完成了 都会来调用
     * 这里也要保存数据，确保最后遗留的数据也存储到数据库 小于BATCH_COUNT的数据会在这里被处理
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    	dealwithFinalData();
        LOGGER.info("所有数据解析完成！");
    }
    /**
     * @Title: dealwithData
     * @Description: 处理已经读取好的数据
     * @Author daiyangyang
     * @DateTime 2019年12月3日 上午11:32:52
     */
    private void dealwithData() {
        LOGGER.info("{}条数据，开始被操作！", dataList.size());
        dealwithBatchCountData(dataList);
        LOGGER.info("操作成功！");
        //防止多sheet页的时候出现问题
        dataList.clear();
    }
    
    
    /**
     * 多sheet页时候这里也需要清除，每个sheet页是单独处理的
     * @Title: dealwithData
     * @Description: 处理已经读取好的数据
     * @Author daiyangyang
     * @DateTime 2019年12月3日 上午11:32:52
     */
    protected void dealwithFinalData() {
        LOGGER.info("{}条数据，开始被操作！", dataList.size());
        dealwithSheetFinalData(dataList);
        LOGGER.info("操作成功！");
        //防止多sheet页的时候出现问题
        dataList.clear();
    }
    
    
    
    /**
     * @Description: 处理每BATCH_COUNT条数据
     * @Author daiyangyang
     * @DateTime 2019年12月3日 下午2:18:36
     */
    public abstract void dealwithBatchCountData(List<T> dataList);
    
    
    /**
     * @Description: 处理一个sheet页中最后的xx条数据 由一个sheet页的总数和BATCH_COUNT决定
     * @Author daiyangyang
     * @DateTime 2019年12月3日 下午2:16:02
     */
    public abstract void dealwithSheetFinalData(List<T> dataList);
    
}
