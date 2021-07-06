package com.example.adverity.repository;

import com.example.adverity.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    List<Statistic> findAllByDatasourceAndDateBetween(String datasource, LocalDate start, LocalDate end);
    List<Statistic> findAllByDatasourceAndCampaign(String datasource, String campaign);
    @Query("select s.date, SUM (s.impressions) from Statistic AS s group by s.date order by s.date ASC")
    List<Object[]> groupByImpressions();

}
