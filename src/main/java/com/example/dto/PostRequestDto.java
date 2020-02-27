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
@ApiModel("POST请求对象")
public class PostRequestDto extends DataTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "爬取网页",name = "page")
    private String page;
    @ApiModelProperty(value = "爬取内容",name = "content")
    private String content;
    @ApiModelProperty(value = "json路径",name = "jsonPathStr")
    private String jsonPathStr;
    @ApiModelProperty(value = "递增参数",name = "ascParam")
    private String ascParam;
    @ApiModelProperty(value = "最大递增参数",name = "maxAscParam")
    private String maxAscParam;
}
