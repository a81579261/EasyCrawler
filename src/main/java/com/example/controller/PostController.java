package com.example.controller;

import com.example.dto.PostRequestDto;
import com.example.service.PostService;
import com.example.utils.Response.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/service/post")
@Api(value = "POST请求管理器")
public class PostController {

    @Autowired
    PostService postService;

    @ApiOperation(value = "POST请求结果爬取")
    @RequestMapping(value = "/postData",method = RequestMethod.POST)
    @ApiParam(name = "POST请求对象", value = "POST请求对象",type = "Object",required = true)
    public ResBody post(@RequestBody PostRequestDto postRequestDto) {
        String exportKey = postService.post(postRequestDto);
        return ResBody.buildSuccessResBody(exportKey);
    }
}
