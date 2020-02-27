package com.example.dto;

import com.example.entity.DataTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Agus
 */
@Data
@ApiModel("GET请求对象")
public class RequestDto extends DataTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "爬取网页",name = "page")
    String page;
    @ApiModelProperty(value = "爬取链接1",name = "links1")
    String links1;
    @ApiModelProperty(value = "爬取链接2",name = "links2")
    String links2;
    @ApiModelProperty(value = "爬取链接3",name = "links3")
    String links3;
    @ApiModelProperty(value = "爬取链接4",name = "links4")
    String links4;
    @ApiModelProperty(value = "爬取深度",name = "depth")
    int depth;
}
