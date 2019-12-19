package com.example.utils.easyexcel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * 直接用map接收数据
 * @Description: 
 * @Author daiyangyang
 * @DateTime 2019年12月3日 下午3:10:29
 */
public class NoModleDataListener extends BaseDataListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoModleDataListener.class);
	public static final int BATCH_COUNT = 200;
	
	public NoModleDataListener() {
		super(BATCH_COUNT);
	}
	
	public NoModleDataListener(int batchCount) {
		super(batchCount);
	}
	
	/**
	 * 处理每BATCH_COUNT条数据
	 */
	@Override
	public void dealwithBatchCountData(List<Map<Integer, String>> dataList) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 处理sheet也最后xx条数据
	 */
	@Override
	public void dealwithSheetFinalData(List<Map<Integer, String>> dataList) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
