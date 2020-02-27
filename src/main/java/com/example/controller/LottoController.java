package com.example.controller;

import com.example.entity.Lottery;
import com.example.service.BuildLottoService;
import com.example.utils.Response.ResBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Agus
 */
@Slf4j
@RestController
@RequestMapping("/service/lotto")
@Api(value = "大X透管理器")
public class LottoController {

    @Autowired
    BuildLottoService buildLottoService;

    @ApiOperation(value = "更新大X透数据")
    @RequestMapping(value = "/updateLotto",method = RequestMethod.GET)
    public ResBody updateLotto() throws Exception {
        buildLottoService.updateLotto(1);
        return ResBody.buildSuccessResBody();
    }

    @ApiOperation(value = "输出大X透数据")
    @RequestMapping(value = "/buildLotteryNo",method = RequestMethod.GET)
    public ResBody buildLotteryNo(){
        List<Lottery> list = buildLottoService.buildLotteryNo();
        return ResBody.buildSuccessResBody(list);
    }
}
