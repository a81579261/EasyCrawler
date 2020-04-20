package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuxiangen
 * @since 2020-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zao_ya")
@Builder
public class ZaoYa implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type= IdType.INPUT)
    private String id;

    @TableField("create_time")
    private Date createTime;

    @TableField("title1")
    private String title1;

    @TableField("content1")
    private String content1;

    @TableField("title2")
    private String title2;

    @TableField("content2")
    private String content2;

    @TableField("title3")
    private String title3;

    @TableField("content3")
    private String content3;

    @TableField("title4")
    private String title4;

    @TableField("content4")
    private String content4;

    @TableField("news_date")
    private String newsDate;
}
