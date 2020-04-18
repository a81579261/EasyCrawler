package com.example.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.dto.PostRequestDto;
import com.example.entity.DataTable;
import com.example.mapper.DataMapper;
import com.example.utils.HttpClientDownloader;
import com.example.utils.JsonPathSelector;
import com.example.utils.RandomUtils;
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
    private String exportKey = new RandomUtils(RandomUtils.AllLetter).generate(64);

    @Autowired
    private DataMapper dataMapper;


    public String post(PostRequestDto postRequestDto) {
        this.postRequestDto = postRequestDto;
        Request request = new Request(postRequestDto.getPage());
        request.setMethod(HttpConstant.Method.POST);
        //获取入参json
        JSONObject jsonObject = JSONObject.fromObject(postRequestDto.getContent());
        //如果有增量参数循环，没有直接执行
        if (StringUtils.isNotBlank(postRequestDto.getAscParam())) {
            String paramStrs[] = postRequestDto.getAscParam().split("\\.");
            if (paramStrs.length == 2) {
                for (int i = jsonObject.getJSONObject(paramStrs[0]).getInt(paramStrs[1]); i < Integer.valueOf(postRequestDto.getMaxAscParam()); i++) {
                    JSONObject temp = jsonObject.getJSONObject(paramStrs[0]);
                    temp.put(paramStrs[1], i);
                    jsonObject.put(paramStrs[0], temp);
                    request.setRequestBody(HttpRequestBody.json(jsonObject.toString(), "utf-8"));
                    Spider.create(this).setDownloader(new HttpClientDownloader()).addRequest(request).run();
                }
            } else if (1 == paramStrs.length){
                for (int i = jsonObject.getInt(paramStrs[0]); i < Integer.valueOf(postRequestDto.getMaxAscParam()); i++) {
                    jsonObject.put(paramStrs[0], i);
                    request.setRequestBody(HttpRequestBody.json(jsonObject.toString(), "utf-8"));
                    Spider.create(this).setDownloader(new HttpClientDownloader()).addRequest(request).run();
                }
            }else {
                return "暂不支持多层";
            }
        } else {
            request.setRequestBody(HttpRequestBody.json(jsonObject.toString(), "utf-8"));
            Spider.create(this).setDownloader(new HttpClientDownloader()).addRequest(request).run();
        }
        return exportKey;
    }


    @Override
    public void process(Page page) {
        List<JSONObject> list = new JsonPathSelector(postRequestDto.getJsonPathStr()).selectJsonList(page.getRawText());
        list.forEach(e -> {
            DataTable dataTable = new DataTable();
            if (StringUtils.isNotBlank(postRequestDto.getColumn1())) {
                dataTable.setColumn1(e.getString(postRequestDto.getColumn1()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn2())) {
                dataTable.setColumn2(e.getString(postRequestDto.getColumn2()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn3())) {
                dataTable.setColumn3(e.getString(postRequestDto.getColumn3()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn4())) {
                dataTable.setColumn4(e.getString(postRequestDto.getColumn4()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn5())) {
                dataTable.setColumn5(e.getString(postRequestDto.getColumn5()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn6())) {
                dataTable.setColumn6(e.getString(postRequestDto.getColumn6()));
            }
            if (StringUtils.isNotBlank(postRequestDto.getColumn7())) {
                dataTable.setColumn7(e.getString(postRequestDto.getColumn7()));
            }
            dataTable.setExportKey(exportKey);
            dataMapper.insert(dataTable);
        });
    }

    @Override
    public Site getSite() {
        return site;
    }
}
