package com.example.adverity.service;

import com.example.adverity.model.Statistic;
import com.example.adverity.repository.StatisticRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        try (CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat)) {
            for (CSVRecord csvRecord : csvParser) {
                Statistic statistic = new Statistic(
                        csvRecord.get("Datasource"),
                        csvRecord.get("Campaign"),
                        LocalDate.parse(csvRecord.get("Daily"), dateTimeFormatter),
                        Long.parseLong(csvRecord.get("Clicks")),
                        Long.parseLong(csvRecord.get("Impressions")));

                repository.saveAndFlush(statistic);
            }
            logger.info("CSV file parsed.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading object list from file " + e.getMessage());
        }
//        } catch (MalformedURLException e) {
//            logger.error("Wrong URL to CSV file ", e);
//        }
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
