package com.example.adverity.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
public class Statistic {
    @GeneratedValue
    @Id
    private Long id;
    @Column
    private String datasource;
    @Column
    private String campaign;
    @Column
    private LocalDate date;
    @Column
    private long clicks;
    @Column
    private long impressions;

    public Statistic(String datasource, String campaign, LocalDate date, long clicks, long impressions) {
        this.datasource = datasource;
        this.campaign = campaign;
        this.date = date;
        this.clicks = clicks;
        this.impressions = impressions;
    }

    public Statistic(Long id, String datasource, String campaign, LocalDate date, long clicks, long impressions) {
        this.id = id;
        this.datasource = datasource;
        this.campaign = campaign;
        this.date = date;
        this.clicks = clicks;
        this.impressions = impressions;
    }

    public String getDatasource() {
        return datasource;
    }

    public String getCampaign() {
        return campaign;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getClicks() {
        return clicks;
    }

    public long getImpressions() {
        return impressions;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public void setImpressions(long impressions) {
        this.impressions = impressions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
