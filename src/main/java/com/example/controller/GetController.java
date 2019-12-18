package com.example.controller;

import com.example.dto.LoginDto;
import com.example.dto.PostRequestDto;
import com.example.dto.RequestDto;
import com.example.service.GetService;
import com.example.service.MovieService;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetController {
    @Autowired
    MovieService movieService;
    @Autowired
    GetService getService;
    @Autowired
    PostService postService;

    @RequestMapping(value = "/getMovie")
    public void myapitest() throws Exception {
        movieService.getMovie();
    }

    @RequestMapping(value = "/getData",method = RequestMethod.POST)
    public void get(@RequestBody RequestDto requestDto) throws Exception {
        getService.get(requestDto);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(@RequestBody LoginDto loginDto){

    }

    @RequestMapping(value = "/postData",method = RequestMethod.POST)
    public void post(@RequestBody PostRequestDto postRequestDto) throws Exception {
        postService.post(postRequestDto);
    }
}
