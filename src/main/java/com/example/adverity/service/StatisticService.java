package com.example.adverity.service;

import com.example.adverity.model.Statistic;
import com.example.adverity.repository.StatisticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticService {
    private final StatisticRepository repository;
    @Value("${csvUrl}")
    private URL url;
    Logger logger = LoggerFactory.getLogger(StatisticService.class);

    public StatisticService(StatisticRepository repository) {
        this.repository = repository;
    }

    public void extractAndSaveData() {
        try {
            var result = CsvService.loadObjectList(url);
            repository.saveAll(result);
        } catch (MalformedURLException e) {
            logger.error("Wrong URL to CSV file ", e);
        }
    }

    public List<Statistic> getStatistics() {
        return repository.findAll();
    }

    public long totalClicks(String dataSource, LocalDate start, LocalDate end) {
        return repository.findAllByDatasourceAndDateBetween(dataSource, start, end)
                .stream()
                .mapToLong(Statistic::getClicks)
                .sum();
    }

    public double clickThroughRate(String dataSource, String campaign) {
        List<Statistic> stats = repository.findAllByDatasourceAndCampaign(dataSource, campaign);
        long clicks = stats.stream().mapToLong(Statistic::getClicks).sum();
        long impressions = stats.stream().mapToLong(Statistic::getImpressions).sum();
        logger.info("CTR -> clicks : " + clicks + " impressions : " + impressions);
        return Math.round(((clicks * 100.0) / impressions) * 100.0) / 100.0;
    }

    public List<Object[]> impressionsDaily() {
        return repository.groupByImpressions();
    }
}
