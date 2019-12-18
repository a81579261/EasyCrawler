package com.example.service;


import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.example.entity.Movie;
import com.example.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MovieService extends RamCrawler {

    @Autowired
    MovieMapper movieMapper;

    public void getMovie() throws Exception {
        seeds.clear();
        addSeed("https://Movie.douban.com/", "moivelist");
        this.start(2);
    }

    @MatchType(types = "moivelist")
    public void visitmoivelist(Page page, CrawlDatums next) {
        //如果是列表页，抽取内容页链接
        //将内容页链接的type设置为content，并添加到后续任务中
        next.add(page.links("li.poster>a"), "moivedetail");
    }

    @MatchType(types = "moivedetail")
    public void visitMoiveDetail(Page page, CrawlDatums next) {
        Movie movie = new Movie();
        movie.setUrl(page.url());
        movie.setTitle(page.selectText("#content>h1"));
        movie.setDirectedby(page.selectText("[rel=v:directedBy]"));
        movie.setSubtitle(page.selectText("div.related-info>h2"));
        movie.setStar(page.selectTextList("[rel=v:starring]").stream().collect(Collectors.joining(";")));
        movie.setSummary(page.selectText("[property=v:summary]"));
        movie.setRating(page.selectText("[property=v:average]"));
        movieMapper.insert(movie);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {

    }
}
