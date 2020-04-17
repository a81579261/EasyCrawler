package com.example.controller;

import com.example.dto.RequestDto;
import com.example.service.JPSickDetailService;
import com.example.service.JPSickService;
import com.example.service.GetService;
import com.example.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Agus
 */
@Slf4j
@RestController
@RequestMapping("/service/get")
@Api(value = "GET请求爬取页面")
public class GetController {
    @Autowired
    MovieService movieService;
    @Autowired
    GetService getService;
    @Autowired
    JPSickService jpSickService;
    @Autowired
    JPSickDetailService jpSickDetailService;

    @ApiOperation(value = "爬取豆瓣电影")
    @RequestMapping(value = "getMovie",method = RequestMethod.GET)
    public void myapitest() throws Exception {
        movieService.getMovie();
    }

    @ApiOperation(value = "GET请求爬取")
    @RequestMapping(value = "getData",method = RequestMethod.POST)
    @ApiParam(name = "GET请求对象", value = "GET请求对象",type = "Object",required = true)
    public void getData(@RequestBody RequestDto requestDto) throws Exception {
        getService.get(requestDto);
    }

    @ApiOperation(value = "爬取https://caloo.jp/achievements/首页分类和名字")
    @RequestMapping(value = "getSickClassAndSickName",method = RequestMethod.POST)
    @ApiParam(name = "GET请求对象", value = "GET请求对象",type = "Object",required = true)
    public void getOnePage2StringList(@RequestBody RequestDto requestDto) throws Exception {
        jpSickService.getSickClassAndSickName(requestDto);
    }

    @ApiOperation(value = "爬取https://caloo.jp/achievements/明细")
    @RequestMapping(value = "getSickDetail",method = RequestMethod.POST)
    @ApiParam(name = "GET请求对象", value = "GET请求对象",type = "Object",required = true)
    public void getSickDetail(@RequestBody RequestDto requestDto) throws Exception {
        jpSickDetailService.getSickDetail(requestDto);
    }
}
