package com.example.service;

import com.example.dto.ZaoYaDTO;
import com.example.entity.ZaoYa;
import com.example.mapper.ZaoYaMapper;
import com.example.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ZaoYaService implements PageModelPipeline<ZaoYaDTO> {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Autowired
    private ZaoYaMapper zaoYaMapper;


    public void getZaoYa() {
        //启动webmagic
        OOSpider.create(site,this,ZaoYaDTO.class)
                .addUrl("https://blog.love2love.top/archives/352.html?from=timeline")
                .thread(5)
                .run();
    }

    @Override
    public void process(ZaoYaDTO zaoYaDTO, Task task) {
        List<String> content1 = new ArrayList<>();
        content1.addAll(zaoYaDTO.getContent11());
        content1.addAll(zaoYaDTO.getContent12());
        content1.addAll(zaoYaDTO.getContent13());
        content1.addAll(zaoYaDTO.getContent14());
        content1.addAll(zaoYaDTO.getContent15());
        ZaoYa zaoYa = ZaoYa.builder()
                .id(IdUtils.getRandomIdByUUID())
                .newsDate(zaoYaDTO.getDate())
                .title1(zaoYaDTO.getTitle1())
                .title2(zaoYaDTO.getTitle2())
                .title3(zaoYaDTO.getTitle3())
                .title4(zaoYaDTO.getTitle4())
                .content1(JSONArray.fromObject(content1).toString())
                .content2(JSONArray.fromObject(zaoYaDTO.getContent2()).toString())
                .content3(JSONArray.fromObject(zaoYaDTO.getContent3()).toString())
                .content4(JSONArray.fromObject(zaoYaDTO.getContent4()).toString())
                .createTime(new Date())
                .build();
        zaoYaMapper.insert(zaoYa);
    }
}
