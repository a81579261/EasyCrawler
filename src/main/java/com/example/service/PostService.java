package com.example.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.dto.PostRequestDto;
import com.example.entity.DataTable;
import com.example.mapper.DataMapper;
import com.example.utils.HttpClientDownloader;
import com.example.utils.JsonPathSelector;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;

@Service
public class PostService implements PageProcessor {

    private Site site = Site.me().setSleepTime(100);
    private PostRequestDto postRequestDto;

    @Autowired
    private DataMapper dataMapper;


    public void post(PostRequestDto postRequestDto) {
        this.postRequestDto = postRequestDto;
        Request request = new Request(postRequestDto.getPage());
        request.setMethod(HttpConstant.Method.POST);
        //获取入参json
        JSONObject jsonObject = JSONObject.fromObject(postRequestDto.getContent());
        //如果有增量参数循环，没有直接执行
        if (StringUtils.isNotEmpty(postRequestDto.getAscParam())) {
            for (int i = jsonObject.getJSONObject("pageInfo").getInt(postRequestDto.getAscParam()); i < Integer.valueOf(postRequestDto.getMaxAscParam()); i++) {
                JSONObject temp = jsonObject.getJSONObject("pageInfo");
                temp.put(postRequestDto.getAscParam(), i);
                jsonObject.put("pageInfo",temp);
                request.setRequestBody(HttpRequestBody.json(jsonObject.toString(), "utf-8"));
                Spider.create(this).setDownloader(new HttpClientDownloader()).addRequest(request).run();
            }
        } else {
            request.setRequestBody(HttpRequestBody.json(jsonObject.toString(), "utf-8"));
            Spider.create(this).setDownloader(new HttpClientDownloader()).addRequest(request).run();
        }
    }


    @Override
    public void process(Page page) {
        List<JSONObject> list = new JsonPathSelector(postRequestDto.getJsonPathStr()).selectJsonList(page.getRawText());
        list.forEach(e -> {
            DataTable dataTable = new DataTable();
            if (StringUtils.isNotEmpty(postRequestDto.getColumn1())) {
                dataTable.setColumn1(e.getString(postRequestDto.getColumn1()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn2())) {
                dataTable.setColumn2(e.getString(postRequestDto.getColumn2()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn3())) {
                dataTable.setColumn3(e.getString(postRequestDto.getColumn3()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn4())) {
                dataTable.setColumn4(e.getString(postRequestDto.getColumn4()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn5())) {
                dataTable.setColumn5(e.getString(postRequestDto.getColumn5()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn6())) {
                dataTable.setColumn6(e.getString(postRequestDto.getColumn6()));
            }
            if (StringUtils.isNotEmpty(postRequestDto.getColumn7())) {
                dataTable.setColumn7(e.getString(postRequestDto.getColumn7()));
            }
            dataMapper.insert(dataTable);
        });
    }

    @Override
    public Site getSite() {
        return site;
    }
}
