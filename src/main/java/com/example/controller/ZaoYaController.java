package com.example.controller;

import com.example.service.ZaoYaService;
import com.example.utils.Response.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/service/ZaoYa")
@Api(value = "早呀请求管理器")
public class ZaoYaController {

    @Autowired
    private ZaoYaService zaoYaService;

    @ApiOperation(value = "早呀日报爬取")
    @RequestMapping(value = "/getZaoYa",method = RequestMethod.GET)
    public ResBody getZaoYa() {
        zaoYaService.getZaoYa();
        return ResBody.buildSuccessResBody();
    }
}
