package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.utils.IdUtils;
import lombok.Data;

@Data
@TableName("movie")
public class Movie {
    private String id;

    private String title;

    private String rating;

    private String star;

    private String directedby;

    private String url;

    private String summary;

    private String subtitle;

    public Movie(){
        setId(IdUtils.getRandomIdByUUID());
    }
}