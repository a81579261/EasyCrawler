package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.DataTable;
import org.springframework.stereotype.Repository;

@Repository
public interface DataMapper extends BaseMapper<DataTable> {
}