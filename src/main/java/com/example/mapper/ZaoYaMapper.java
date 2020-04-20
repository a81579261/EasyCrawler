package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ZaoYa;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wuxiangen
 * @since 2020-04-18
 */
@Mapper
@Repository
public interface ZaoYaMapper extends BaseMapper<ZaoYa> {

}
