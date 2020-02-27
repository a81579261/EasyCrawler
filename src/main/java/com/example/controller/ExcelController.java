package com.example.controller;

import com.example.service.ExcelService;
import com.example.utils.Response.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Agus
 */
@Slf4j
@RestController
@RequestMapping("/service/excel")
@Api(value = "Excel管理器")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @ApiOperation(value = "导出Excel")
    @RequestMapping(value = "exportExcel", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "key", type = "String", value = "key")
    })
    public ResBody exportExcel(@RequestParam("key") String key) throws Exception {
        Optional.ofNullable(key).orElseThrow(() -> new Exception("key不能为空"));
        excelService.exportExcel(key);
        return ResBody.buildSuccessResBody();
    }
}
