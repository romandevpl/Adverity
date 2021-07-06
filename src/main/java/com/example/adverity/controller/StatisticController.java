package com.example.adverity.controller;

import com.example.adverity.model.Statistic;
import com.example.adverity.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class StatisticController {
    private final StatisticService statisticService;

    Logger logger = LoggerFactory.getLogger(StatisticController.class);

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
        statisticService.extractAndSaveData();
    }

    @GetMapping("/statistics")
    public List<Statistic> getStatistics() {
        return statisticService.getStatistics();
    }

    @PostMapping("/totalClicks")
    public long getClicks(@RequestParam("datasource") String datasource,
                              @RequestParam("startDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                              @RequestParam("endDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) {
        logger.info("Request: " + datasource + " " + startDate + " " + endDate);
        return statisticService.totalClicks(datasource, startDate, endDate);
    }

    @PostMapping("/clickThroughRate")
    public String getClickThroughRate(@RequestParam("datasource") String datasource,
                                @RequestParam("campaign") String campaign) {
        logger.info("Request: " + datasource + " " + campaign);
        return statisticService.clickThroughRate(datasource, campaign) + " %";
    }

    @GetMapping("/impressions")
    public List<Object[]> getImpressions() {
        return statisticService.impressionsDaily();
    }
}
