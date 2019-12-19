package com.example.utils.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EasyExcelUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelUtil.class);

	/**
	 * 最简单的读
	 * <p>
	 * 1. 创建excel对应的实体对象 参照{@link}
	 * <p>
	 * 2.由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link}
	 * <p>
	 * 3. 直接读即可 有个很重要的点 DemoDataListener
	 * 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去 这里
	 * 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
	 * 
	 * @Title: simpleRead
	 * @Description:
	 * @Author daiyangyang
	 * @param fileName     例如 /xxx/xxx.xlsx
	 * @param dataListener 用于数据监听处理
	 * @param clazz        解析实体的类对象
	 * @param allSheet     true 读取所有sheet里面的内容 false 读取第一个sheet页
	 * @DateTime 2019年12月3日 下午1:49:53
	 */
	public static <T extends EasyBaseModel> void simpleRead(String fileName, BaseDataListener<T> dataListener, Class<T> clazz,
															boolean allSheet) {
		checkFileName(fileName);
		checkClazz(clazz);
		File file = new File(fileName);
		if (allSheet) {
			// 读取全部sheet,这里需要注意 BaseDataListener的doAfterAllAnalysed
			EasyExcel.read(file, clazz, dataListener).doReadAll();
		} else {
			EasyExcel.read(file, clazz, dataListener).sheet().doRead();
		}
	}
	/**
	 * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
	 * @Description: 读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午6:54:24
	 * @param <T>
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param clazz    解析实体的类对象
	 * @return
	 */
	public static <T extends EasyBaseModel> List<T> synchronousRead(String fileName, Class<T> clazz) {
		checkFileName(fileName);
		checkClazz(clazz);
		return EasyExcel.read(fileName).head(clazz).sheet().doReadSync();
	}

	/**
	 * @Description:读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午6:56:38
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @return 返回每条数据的键值对 表示所在的列 和所在列的值
	 */
	public static  List<Map<Integer, String>> synchronousRead(String fileName) {
		checkFileName(fileName);
		return EasyExcel.read(fileName).sheet().doReadSync();
	}

	/**
	 * @Description: 不创建对象的读，不是特别推荐使用，都用String接收对日期的支持不是很好 默认异步读取excel
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午3:20:19
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param noModleDataListener 可以继承它，然后重写处理数据的方法
	 */
	public static void noModleRead(String fileName, NoModleDataListener noModleDataListener,boolean allSheet) {
		checkFileName(fileName);
		noModleDataListener = ifNullSetNoModleDataListener(noModleDataListener);
		File file = new File(fileName);
		if(allSheet) {
			EasyExcel.read(file, noModleDataListener).doReadAll();
		}else {
			EasyExcel.read(file, noModleDataListener).sheet().doRead();
		}
	}

	/**
	 * @Description: 需要指定写用哪个class去写，然后写到第一个sheet 然后文件流会自动关闭
	 * <p>
	 *   1. 创建excel对应的实体对象 参照{@link}
	 * <p>
	 *   2.直接写即可
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午4:53:54
	 * @param <T>
	 * @param excludeColumnFiledNames 根据用户传入字段,要忽略的字段
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param dataList 数据集合
	 * @param sheetName 第一个sheet页的名称
	 */
	public static <T extends EasyBaseModel> void simpleWrite(String fileName,String sheetName,Set<String> excludeColumnFiledNames, 
			List<T> dataList, Class<T> clazz) {
		checkSheetName(sheetName);
		checkFileName(fileName);
		checkDataList(dataList);
		checkClazz(clazz);
		if(CollectionUtils.isEmpty(excludeColumnFiledNames)) {
			EasyExcel.write(fileName, clazz).sheet(sheetName).doWrite(dataList);
		}else {
			EasyExcel.write(fileName, clazz).excludeColumnFiledNames(excludeColumnFiledNames).sheet(sheetName).doWrite(dataList);
		}
	}
	/**
	 * @Description: 根据模板写入  需要指定写用哪个class去写，然后写到第一个sheet  然后文件流会自动关闭
	 * <p>
	 * 1. 创建excel对应的实体对象 参照{@link}
	 * <p>
	 * 2.使用{@link ExcelProperty}注解指定写入的列
	 * <p>
	 * 3. 使用withTemplate 写取模板 
	 * <p>
	 * 4. 直接写即可
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午5:29:47
	 * @param <T>
	 * @param templateFileName 模板文件的路径 例如 /xxx/xxx.xlsx
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param dataList 数据集合
	 * @param clazz 解析实体的类对象
	 * @param sheetName 第一个sheet页的名称
	 */
	public static <T extends EasyBaseModel> void templateWrite(String templateFileName, String fileName,String sheetName, List<T> dataList,
			Class<T> clazz) {
		checkSheetName(sheetName);
		checkFileName(templateFileName);
		checkFileName(fileName);
		checkDataList(dataList);
		checkClazz(clazz);
		EasyExcel.write(fileName, clazz).withTemplate(templateFileName).sheet(sheetName).doWrite(dataList);
	}

	/**
	 * @Description: 动态头，实时生成头写入 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table
	 *               不写入头的方式 去写入数据 1. 创建excel对应的实体对象 参照{@link} 2.
	 *               然后写入table即可
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午5:34:28
	 * @param <T>
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param headlist     excle头集合
	 * @param dataList 数据集合
	 * @param clazz 解析实体的类对象
	 */
	public static <T extends EasyBaseModel> void dynamicHeadWrite(String fileName,String sheetName, List<List<String>> headlist, List<T> dataList,
			Class<T> clazz) {
		checkFileName(fileName);
		checkSheetName(sheetName);
		checkDataList(dataList);
		checkClazz(clazz);
		checkHeadlist(headlist);
		EasyExcel.write(fileName)
				.head(headlist).sheet(sheetName)
				.doWrite(dataList);
	}
	/**
	 * 
	 * @Title: 不创建对象的写
	 * @Description: 
	 * @Author daiyangyang
	 * @DateTime 2019年12月4日 上午9:03:24
	 * @param fileName 例如 /xxx/xxx.xlsx
	 * @param sheetName 第一个sheet页的名称
	 * @param headlist 头集合
	 * @param dataList 数据集合
	 */
	public static void noModleWrite(String fileName,String sheetName ,List<List<String>> headlist, List<List<Object>> dataList) {
		EasyExcel.write(fileName).head(headlist).sheet(sheetName).doWrite(dataList);
	}

	/**
	 * @Title: 合并的写
	 * @Author wuxiangen
	 * @param fileName
	 * @param sheetName
	 * @param excludeColumnFiledNames
	 * @param dataList
	 * @param clazz
	 */
	public static <T extends EasyBaseModel> void mergeWrite(String fileName, String sheetName, Set<String> excludeColumnFiledNames,
															List<T> dataList, Class<T> clazz, AbstractMergeStrategy abstractMergeStrategy) {
		checkSheetName(sheetName);
		checkFileName(fileName);
		checkDataList(dataList);
		checkClazz(clazz);
		if (CollectionUtils.isEmpty(excludeColumnFiledNames)) {
			EasyExcel.write(fileName, clazz).sheet(sheetName).registerWriteHandler(abstractMergeStrategy).doWrite(dataList);
		} else {
			EasyExcel.write(fileName, clazz).excludeColumnFiledNames(excludeColumnFiledNames).sheet(sheetName).registerWriteHandler(abstractMergeStrategy).doWrite(dataList);
		}
	}

	/**
	 * @Title: checkFileName
	 * @Description:
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午5:58:53
	 * @param fileName 例如 /xxx/xxx.xlsx
	 */
	private static void checkFileName(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return;
		}
	}
	/**
	 * 
	 * @Title: noModleDataListener为空的时候设置默认的DefaultDataListener
	 * @Description: 
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午7:05:23
	 * @param noModleDataListener 例如{@link NoModleDataListener}
	 * @return
	 */
	private static  NoModleDataListener ifNullSetNoModleDataListener(NoModleDataListener noModleDataListener) {
		if (noModleDataListener == null) {
			return new NoModleDataListener();
		}
		return noModleDataListener;
	}
	/**
	 * @Description: 校验clazz
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午7:15:31
	 * @param clazz
	 */
	private static void checkClazz(Class<?> clazz) {
		if(clazz == null) {
			return;
		}
	}
	/**
	 * @Description: 校验sheet页的名称
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午7:20:05
	 * @param sheetName
	 */
	private static void checkSheetName(String sheetName) {
		if(sheetName == null) {
			return;
		}
	}
	/**
	 * @Description: 校验数据对象
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午7:22:07
	 * @param dataList
	 */
	public static void checkDataList(List<?> dataList) {
		if(CollectionUtils.isEmpty(dataList)) {
			return;
		}
	}
	/**
	 * @Description: 检验表头
	 * @Author daiyangyang
	 * @DateTime 2019年12月3日 下午7:34:16
	 * @param sheetlist
	 */
	private static void checkHeadlist(List<List<String>> sheetlist) {
		if(CollectionUtils.isEmpty(sheetlist)) {
			return;
		}
	}
}
