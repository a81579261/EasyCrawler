package com.example.service;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.dto.RequestDto;
import com.example.entity.DataTable;
import com.example.mapper.DataMapper;
import com.example.utils.HttpRequester;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JPSickService extends RamCrawler {

    @Autowired
    DataMapper dataMapper;

    private RequestDto requestDto;

    private int depth;

    public void getSickClassAndSickName(RequestDto dto) throws Exception {
        seeds.clear();
        this.requestDto = dto;
        this.depth = requestDto.getDepth();
        setRequester(new HttpRequester());
        addSeed(requestDto.getPage(), "content");
        //默认是多线程的，改为单线程，防止503
        setThreads(1);
        this.start(depth);
    }

    @MatchType(types = "content")
    public void visitContent(Page page, CrawlDatums next) {
        for (int i = 1; i < 18; i++) {
            String column1 = "#sc" + i + "> h2";
            for(int j = 1;j<99;j++){
                String column2 = "#sc" + i + "> ul > li:nth-child("+j+")> a";
                String column3 = "#sc" + i + "> ul > li:nth-child("+j+")> a";
                try {
                    String name = page.selectText(column2);
                    DataTable dataTable = new DataTable();
                    dataTable.setUrl(page.url());
                    if (StringUtils.isNotBlank(column1)) {
                        dataTable.setColumn1(page.selectText(column1).length() > 1000 ? page.selectText(column1).substring(0, 999) : page.selectText(column1));
                    }
                    dataTable.setColumn2(name);
                    Elements as = page.doc().select(column3);
                    //获取url
                    for (Element a : as) {
                        if (a.hasAttr("href")) {
                            String href = a.attr("abs:href");
                            dataTable.setColumn3(href);
                        }
                    }
                    dataMapper.insert(dataTable);
                }catch (Exception e){
                    break;
                }
            }

        }
    }

    @Override
    public void visit(Page page, CrawlDatums next) {

    }
}
