package com.example.adverity.service;

import com.example.adverity.model.Statistic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvServiceTest {

    @Test
    void shouldExtractObjectsFromCsvFile() throws MalformedURLException, IOException {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        Statistic firstStat = new Statistic("Google Ads", "Adventmarkt Touristik",
                LocalDate.parse("11/12/19", formatter), 7, 22425);

        String urlString = "src/test/resources/shortStatistics.csv";
        File testFile = new File(urlString);
        InputStream testStream = new FileInputStream(testFile);

        URL url = getMockUrl();
        when(url.openStream()).thenReturn(testStream);
        //when
        var result = CsvService.loadObjectList(url);
        //then
        assertEquals(result.size(), 190);
        assertEquals(result.get(0).getDatasource(), firstStat.getDatasource());
        assertEquals(result.get(0).getCampaign(), firstStat.getCampaign());
    }

    private URL getMockUrl() throws MalformedURLException {
        URLConnection mockUrlCon = mock(URLConnection.class);
        URLStreamHandler stubUrlHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return mockUrlCon;
            }
        };
        return new URL("", "", 99, "", stubUrlHandler);
    }
}