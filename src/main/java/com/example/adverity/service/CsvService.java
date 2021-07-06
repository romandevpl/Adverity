package com.example.adverity.service;

import com.example.adverity.model.Statistic;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    public static List<Statistic> loadObjectList(URL url) throws MalformedURLException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        List<Statistic> statistics = new ArrayList<Statistic>();

        try (CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat)) {
            for (CSVRecord csvRecord : csvParser) {
                Statistic statistic = new Statistic(
                        csvRecord.get("Datasource"),
                        csvRecord.get("Campaign"),
                        LocalDate.parse(csvRecord.get("Daily"), dateTimeFormatter),
                        Long.parseLong(csvRecord.get("Clicks")),
                        Long.parseLong(csvRecord.get("Impressions")));

                statistics.add(statistic);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while loading object list from file " + e.getMessage());
        }
        return statistics;
    }
}
