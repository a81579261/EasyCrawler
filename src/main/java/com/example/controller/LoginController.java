package com.example.controller;

import com.example.dto.LoginDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Agus
 */
@Slf4j
@RestController
@RequestMapping("/service/login")
@Api(value = "登录管理器")
public class LoginController {

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public void login(@RequestBody LoginDto loginDto){

    }
}
