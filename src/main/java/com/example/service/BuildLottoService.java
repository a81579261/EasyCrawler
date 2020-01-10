package com.example.service;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.ram.RamCrawler;
import com.example.entity.Lottery;
import com.example.mapper.LotteryMapper;
import com.example.utils.ListTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BuildLottoService extends RamCrawler {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private LotteryMapper lotteryMapper;

    private int pageIndex;

    public void updateLotto(int pageIndex) throws Exception {
        seeds.clear();
        this.pageIndex = pageIndex;
        if (pageIndex == 1) {
            this.addSeed("http://www.lottery.gov.cn/historykj/history.jspx?_ltype=dlt");
        } else {
            String seedUrl = String.format("http://www.lottery.gov.cn/historykj/history_%d.jspx?_ltype=dlt", pageIndex);
            this.addSeed(seedUrl);
        }
        this.start(1);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        //获取页面数据，组装成lotteryList
        List<String> list = page.selectTextList("div.result > table > tbody > tr");
        List<Lottery> lotteryList = new ArrayList<>();
        list.forEach(e -> {
            Lottery lottery = new Lottery();
            String[] strs = e.split("\\s+");
            List<String> stringList = Arrays.stream(strs).collect(Collectors.toList());
            lottery.setCode(stringList.get(0));
            lottery.setRed1(Integer.valueOf(stringList.get(1)));
            lottery.setRed2(Integer.valueOf(stringList.get(2)));
            lottery.setRed3(Integer.valueOf(stringList.get(3)));
            lottery.setRed4(Integer.valueOf(stringList.get(4)));
            lottery.setRed5(Integer.valueOf(stringList.get(5)));
            lottery.setBlue1(Integer.valueOf(stringList.get(6)));
            lottery.setBlue2(Integer.valueOf(stringList.get(7)));
            try {
                lottery.setOpenDate(DateFormat.getDateInstance().parse(stringList.get(stringList.size() - 1)));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            lottery.setCreateTime(new Date());
            lotteryList.add(lottery);
        });
        Map<String, Object> map = new HashMap<>();
        List<Lottery> dbList = lotteryMapper.selectByMap(map);
        try {
            //比较是否有新增的，有则递归，没有则结束
            List<Lottery> addList = ListTools.getInFirstNotInSecondList(lotteryList, dbList, "code");
            if (addList.size() > 0) {
                addList.forEach(e -> lotteryMapper.insert(e));
                this.updateLotto(pageIndex + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Lottery> buildLotteryNo() {
        List<Lottery> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<Lottery> dbList = lotteryMapper.selectByMap(map);
        //类似于SELECT red1,COUNT(1) FROM `lottery` GROUP BY red1
        Map<Integer, Long> red1Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getRed1, Collectors.counting()));
        Map<Integer, Long> red2Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getRed2, Collectors.counting()));
        Map<Integer, Long> red3Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getRed3, Collectors.counting()));
        Map<Integer, Long> red4Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getRed4, Collectors.counting()));
        Map<Integer, Long> red5Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getRed5, Collectors.counting()));
        Map<Integer, Long> blue1Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getBlue1, Collectors.counting()));
        Map<Integer, Long> blue2Map = dbList.stream().collect(Collectors.groupingBy(Lottery::getBlue2, Collectors.counting()));
        Map<Integer, Long> redSumMap = new HashMap<>();
        Map<Integer, Long> blueSumMap = new HashMap<>();
        for (int i = 1; i < 36; i++) {
            Long red1MapValue = red1Map.get(i) == null ? 0L : red1Map.get(i);
            Long red2MapValue = red2Map.get(i) == null ? 0L : red2Map.get(i);
            Long red3MapValue = red3Map.get(i) == null ? 0L : red3Map.get(i);
            Long red4MapValue = red4Map.get(i) == null ? 0L : red4Map.get(i);
            Long red5MapValue = red5Map.get(i) == null ? 0L : red5Map.get(i);
            Long redSum = red1MapValue + red2MapValue + red3MapValue + red4MapValue + red5MapValue;
            redSumMap.put(i, redSum);
            if (i < 13) {
                Long blue1MapValue = blue1Map.get(i) == null ? 0L : blue1Map.get(i);
                Long blue2MapValue = blue2Map.get(i) == null ? 0L : blue2Map.get(i);
                Long blueSum = blue1MapValue + blue2MapValue;
                blueSumMap.put(i, blueSum);
            }
        }
        logger.info("红球总和:{}", redSumMap);
        logger.info("蓝球总和:{}", blueSumMap);
        //升序获取出现频率最少的5个红球，2个篮球
        //构建红球list
        List<LotteryTemp> redLotteryTemps = new ArrayList<>();
        buildList(redSumMap, redLotteryTemps);
        List<LotteryTemp> blueLotteryTemps = new ArrayList<>();
        buildList(blueSumMap, blueLotteryTemps);
        //红球升序
        List<LotteryTemp> redLotteryTempsAsc = redLotteryTemps.stream().sorted(Comparator.comparingLong(LotteryTemp::getValue)).collect(Collectors.toList());
        //红球降序
        List<LotteryTemp> redLotteryTempsDesc = redLotteryTemps.stream().sorted(Comparator.comparingLong(LotteryTemp::getValue).reversed()).collect(Collectors.toList());
        //蓝球降序
        List<LotteryTemp> blueLotteryTempsDesc = blueLotteryTemps.stream().sorted(Comparator.comparingLong(LotteryTemp::getValue).reversed()).collect(Collectors.toList());
        //蓝球升序
        List<LotteryTemp> blueLotteryTempsAsc = blueLotteryTemps.stream().sorted(Comparator.comparingLong(LotteryTemp::getValue)).collect(Collectors.toList());
        Lottery lotteryAsc = new Lottery();
        Lottery lotteryDesc = new Lottery();
        //出现频率最少的
        lotteryAsc.setRed1(redLotteryTempsAsc.get(0).getKey());
        lotteryAsc.setRed2(redLotteryTempsAsc.get(1).getKey());
        lotteryAsc.setRed3(redLotteryTempsAsc.get(2).getKey());
        lotteryAsc.setRed4(redLotteryTempsAsc.get(3).getKey());
        lotteryAsc.setRed5(redLotteryTempsAsc.get(4).getKey());
        lotteryAsc.setBlue1(blueLotteryTempsAsc.get(0).getKey());
        lotteryAsc.setBlue2(blueLotteryTempsAsc.get(1).getKey());
        list.add(lotteryAsc);
        //出现频率最多的
        lotteryDesc.setRed5(redLotteryTempsDesc.get(4).getKey());
        lotteryDesc.setRed4(redLotteryTempsDesc.get(3).getKey());
        lotteryDesc.setRed3(redLotteryTempsDesc.get(2).getKey());
        lotteryDesc.setRed2(redLotteryTempsDesc.get(1).getKey());
        lotteryDesc.setRed1(redLotteryTempsDesc.get(0).getKey());
        lotteryDesc.setBlue2(blueLotteryTempsDesc.get(1).getKey());
        lotteryDesc.setBlue1(blueLotteryTempsDesc.get(0).getKey());
        list.add(lotteryDesc);

        return list;
    }

    private class LotteryTemp {
        private Integer key;
        private Long value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }

    /**
     * 将map构建成tempList
     *
     * @param map
     * @param list
     */
    private void buildList(Map<Integer, Long> map, List<LotteryTemp> list) {
        for (Map.Entry<Integer, Long> entry : map.entrySet()) {
            LotteryTemp lotteryTemp = new LotteryTemp();
            lotteryTemp.setKey(entry.getKey());
            lotteryTemp.setValue(entry.getValue());
            list.add(lotteryTemp);
        }
    }
}
