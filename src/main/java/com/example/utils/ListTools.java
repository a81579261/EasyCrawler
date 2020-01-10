package com.example.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxiangen on 2018/5/7.
 */
public class ListTools {
    /**
     * 计算列表aList相对于bList的增加的情况，兼容任何类型元素的列表数据结构
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回增加的元素组成的列表
     */
    public static <E> List<E> getAddaListThanbList(List<E> aList, List<E> bList){
        List<E> addList = new ArrayList<E>();
        for (int i = 0; i < aList.size(); i++){
            if(!myListContains(bList, aList.get(i))){
                addList.add(aList.get(i));
            }
        }
        return addList;
    }

    /**
     * 计算列表aList相对于bList的减少的情况，兼容任何类型元素的列表数据结构
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回减少的元素组成的列表
     */
    public static <E> List<E> getReduceaListThanbList(List<E> aList, List<E> bList){
        List<E> reduceaList = new ArrayList<E>();
        for (int i = 0; i < bList.size(); i++){
            if(!myListContains(aList, bList.get(i))){
                reduceaList.add(bList.get(i));
            }
        }
        return reduceaList;
    }

    /**
     * 判断元素element是否是sourceList列表中的一个子元素
     * @param sourceList 源列表
     * @param element 待判断的包含元素
     * @return 包含返回 true，不包含返回 false
     */
    private static <E> boolean myListContains(List<E> sourceList, E element) {
        if (sourceList == null || element == null){
            return false;
        }
        if (sourceList.isEmpty()){
            return false;
        }
        for (E tip : sourceList){
            if(element.equals(tip)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取在第一个集合中但是不在第二个集合中的数据
     * @param first 第一个集合
     * @param second 第二个集合
     * @param compareFieldName 需要比较的属性名称
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> List<T> getInFirstNotInSecondList(List<
            T> first, List<T> second, String compareFieldName) throws IllegalArgumentException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        List<T> result = new ArrayList<>();
        if(isBaseEqual(first, second)){
            Map<String, String> listMap = new HashMap<>();
            String temp;
            for(T t : second){
                temp = String.valueOf(getFieldValue(t, compareFieldName));
                listMap.put(temp, temp);
            }
            for(T t : first){
                temp = String.valueOf(getFieldValue(t, compareFieldName));
                if(listMap.get(temp) == null){
                    result.add(t);
                }
            }
        }else if(first != null && first.size() != 0 && (second == null || second.size() == 0)){
            result = first;
        }
        return result;
    }
    private static <T> boolean isBaseEqual(List<T> first, List<T> second){
        if(first == null || second ==  null || first.size() == 0 || second.size() == 0){
            return false;
        }
        return true;
    }
    public static <T> Object getFieldValue(T t, String fieldName) throws IllegalArgumentException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        String methodName = getGetMethodRealName(fieldName);
        if(methodName == null){
            throw new  IllegalArgumentException("外部订单号不能为空");
        }
        Class<?> clazz = t.getClass();
        Method getMethod = clazz.getMethod(methodName);
        return getMethod.invoke(t);
    }
    private static String GET_METHOD_PREFIX = "get";
    private static String getGetMethodRealName(String fieldName){
        if(fieldName == null || fieldName.length() ==0){
            return null;
        }
        String getMethodRealName = GET_METHOD_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return getMethodRealName;
    }


    /**
     * 获取两个集合中共有元素集合，通过比对指定的属性名称所对应的属性值
     * @param first 集合一,新的List
     * @param second 集合二，旧的List
     * @param compareFieldName 需要做比较的属性名称
     * @return 两个集合中共有元素
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> List<T> getCommonElements(List<T> first, List<T> second,String compareFieldName) throws IllegalArgumentException, NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException{
        List<T> result = new ArrayList<>();
        if(isBaseEqual(first, second)){
            Map<String, List<T>> listMap = new HashMap<>();
            String temp;
            for(T t : second){
                temp = String.valueOf(getFieldValue(t, compareFieldName));
                if(listMap.get(temp) == null){
                    listMap.put(temp, new ArrayList<T>());
                }
                listMap.get(temp).add(t);
            }
            for(T t : first){
                temp = String.valueOf(getFieldValue(t, compareFieldName));
                if(listMap.get(temp) != null){
                    result.add(t);
//                    result.addAll(listMap.get(temp));
                }
            }
        }
        return result;
    }

}
