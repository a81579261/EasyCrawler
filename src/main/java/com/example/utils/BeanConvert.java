package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanConvert {

    //log日志
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConvert.class);

    /**
     * 将List<Object> 转换为List<Bean>
     * @param sources 源对象
     * @param targetClass 目标类
     * @param <T>
     * @return
     */
    public static <T> List<T> objectConvertBean(List<?> sources, Class<T> targetClass) {
        List<?> sourcesObj = sources;
        if (sourcesObj == null) {
            sourcesObj = Collections.emptyList();
        }
        List<T> targets = new ArrayList<>(sourcesObj.size());
        convert(sourcesObj, targets, targetClass);
        return targets;
    }

    /**
     * 复制源对象到目的对象
     * 注意：
     *   org.springframework.beans.BeanUtils.copyProperties 是一个Spring提供的名称相同的工具类
     *   但它不支持类型自动转换，如果某个类型属性不同，则不予转换那个属性
     *   org.apache.commons.beanutils.BeanUtils 是一个Apache提供的名称相同的工具类
     *   支持类型自动转换，如Date类型会自动转换为字符串
     * @param sources  源对象
     * @param targets 目的对象
     * @param targetClass 目标类
     * @param <T>
     */
    private static <T> void convert(List<?> sources, List<T> targets, Class<T> targetClass) {
        if (sources == null) {
            return;
        }
        if (targets == null) {
            return;
        }
        targets.clear();//提高效率
        for (Object obj : sources) {
            try {
                T target = targetClass.newInstance();
                targets.add(target);
                BeanUtils.copyProperties(obj, target);
            } catch (Exception e) {
                LOGGER.error("BeanConvert-->Exception:", e);
                return;
            }
        }
    }

}