package com.example.entity;

import com.example.utils.IdUtils;
import lombok.Data;

import java.util.Date;

@Data
public class Lottery {
    private String id;

    private Date createTime;

    private String code;

    private Integer red1;

    private Integer red2;

    private Integer red3;

    private Integer red4;

    private Integer red5;

    private Integer blue1;

    private Integer blue2;

    private Date openDate;

    public Lottery() {
        setId(IdUtils.getRandomIdByUUID());
    }
}