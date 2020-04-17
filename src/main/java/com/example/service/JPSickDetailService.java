package com.example.service;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.dto.RequestDto;
import com.example.entity.DataTable;
import com.example.mapper.DataMapper;
import com.example.utils.HttpRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JPSickDetailService extends RamCrawler {

    @Autowired
    DataMapper dataMapper;

    private RequestDto requestDto;

    private int depth;

    public void getSickDetail(RequestDto dto) throws Exception {
        seeds.clear();
        this.requestDto = dto;
        this.depth = requestDto.getDepth();
        setRequester(new HttpRequester());
        if (1 < depth){
            addSeed(requestDto.getPage(), "depth1");
        }else {
            addSeed(requestDto.getPage(),"content");
        }
        //默认是多线程的，改为单线程，防止503
        setThreads(1);
        this.start(depth);
    }

    @MatchType(types = "depth1")
    public void visitDepth1(Page page, CrawlDatums next) throws Exception {
        if (StringUtils.isBlank(requestDto.getLinks1())){
            throw new Exception("depth1 can not be null!");
        }else {
            if (2 < depth){
                //如果是列表页，抽取内容页链接
                //将内容页链接的type设置为content，并添加到后续任务中
                next.add(page.links(requestDto.getLinks1()), "depth2");
            }else {
                next.add(page.links(requestDto.getLinks1()), "content");
            }

        }
    }
    @MatchType(types = "depth2")
    public void visitDepth2(Page page, CrawlDatums next) throws Exception {
        if (StringUtils.isBlank(requestDto.getLinks2())){
            throw new Exception("depth2 can not be null!");
        }else {
            if (3 < depth){
                //如果是列表页，抽取内容页链接
                //将内容页链接的type设置为content，并添加到后续任务中
                next.add(page.links(requestDto.getLinks2()), "depth3");
            }else {
                next.add(page.links(requestDto.getLinks2()), "content");
            }

        }
    }
    @MatchType(types = "depth3")
    public void visitDepth3(Page page, CrawlDatums next) throws Exception {
        if (StringUtils.isBlank(requestDto.getLinks3())){
            throw new Exception("depth3 can not be null!");
        }else {
            if (4 < depth){
                //如果是列表页，抽取内容页链接
                //将内容页链接的type设置为content，并添加到后续任务中
                next.add(page.links(requestDto.getLinks3()), "depth4");
            }else {
                next.add(page.links(requestDto.getLinks3()), "content");
            }

        }
    }


    @MatchType(types = "content")
    public void visitContent(Page page, CrawlDatums next) {
        DataTable dataTable = new DataTable();
        dataTable.setUrl(page.url());
        if (StringUtils.isNotBlank(requestDto.getColumn1())){
            dataTable.setColumn1(page.selectText(requestDto.getColumn1()).length()>1000 ? page.selectText(requestDto.getColumn1()).substring(0,999) : page.selectText(requestDto.getColumn1()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn2())){
            dataTable.setColumn2(page.selectText(requestDto.getColumn2()).length()>1000 ? page.selectText(requestDto.getColumn2()).substring(0,999) : page.selectText(requestDto.getColumn2()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn3())){
            dataTable.setColumn3(page.selectText(requestDto.getColumn3()).length() > 1000 ? page.selectText(requestDto.getColumn3()).substring(0,999):page.selectText(requestDto.getColumn3()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn4())){
            dataTable.setColumn4(page.selectText(requestDto.getColumn4()).length() > 1000? page.selectText(requestDto.getColumn4()).substring(0,999):page.selectText(requestDto.getColumn4()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn5())){
            dataTable.setColumn5(page.selectText(requestDto.getColumn5()).length() > 1000 ? page.selectText(requestDto.getColumn5()).substring(0,999):page.selectText(requestDto.getColumn5()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn6())){
            dataTable.setColumn6(page.selectText(requestDto.getColumn6()).length() > 1000? page.selectText(requestDto.getColumn6()).substring(0,999):page.selectText(requestDto.getColumn6()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn7())){
            dataTable.setColumn7(page.selectText(requestDto.getColumn7()).length() > 1000 ? page.selectText(requestDto.getColumn7()).substring(0,999):page.selectText(requestDto.getColumn7()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn8())){
            dataTable.setColumn8(page.selectText(requestDto.getColumn8()).length() > 1000 ? page.selectText(requestDto.getColumn8()).substring(0,999): page.selectText(requestDto.getColumn8()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn9())){
            dataTable.setColumn9(page.selectText(requestDto.getColumn9()).length() > 1000 ? page.selectText(requestDto.getColumn9()).substring(0,999):page.selectText(requestDto.getColumn9()));
        }
        if (StringUtils.isNotBlank(requestDto.getColumn10())){
            dataTable.setColumn10(page.selectText(requestDto.getColumn10()).length() > 1000 ? page.selectText(requestDto.getColumn10()).substring(0,999):page.selectText(requestDto.getColumn10()));
        }
        QueryWrapper<DataTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",dataTable.getUrl());
        Integer selectCount = dataMapper.selectCount(queryWrapper);
        if (selectCount == 0){
            dataMapper.insert(dataTable);
        }
    }

    @Override
    public void visit(Page page, CrawlDatums next) {

    }
}
