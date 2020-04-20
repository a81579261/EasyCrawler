package com.example.dto;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

@Data
//匹配https://blog.love2love.top/archives为前缀的所有网址
@TargetUrl("https://blog.love2love.top/archives/*.html")
public class ZaoYaDTO {

    //用ExtractBy注解的字段会被自动抽取并填充
    //默认是xpath语法
    @ExtractBy(value = "#main > div.layout > div.article > article > div.article-meta > span:nth-child(4)",type = ExtractBy.Type.Css)
    private String date;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/ul[1]/li/h3/strong/text()")
    private String title1;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[1]/text()")
    private List<String> content11;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[2]/text()")
    private List<String> content12;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[3]/text()")
    private List<String> content13;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[4]/text()")
    private List<String> content14;
    @ExtractBy(value = "//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[5]/text()")
    private List<String> content15;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/ul[2]/li/h3/strong/text()")
    private String title2;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[6]/text()")
    private List<String> content2;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/ul[3]/li/h3/strong/text()")
    private String title3;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[7]/text()")
    private List<String> content3;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/ul[4]/li/h3/strong/text()")
    private String title4;
    @ExtractBy("//*[@id=\"main\"]/div[2]/div[1]/article/div[4]/p[8]/text()")
    private List<String> content4;
}
