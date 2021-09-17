package com.ticker.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FinnHubService {

    private RestTemplate restTemplate = new RestTemplate();
    private static final String finnHubApiUrl = "https://finnhub.io/api/v1/";

    @Value("${app.keys.finnHubApiKey}")
    private String finnHubApiKey;

    @Cacheable("finnHubQuote")
    public Map getQuote(String symbol) {
        Map map = (Map) restTemplate
            .getForEntity(String.format("%squote?token=%s&symbol=%s", finnHubApiUrl, finnHubApiKey, symbol), Map.class)
            .getBody();

        map.put("tickerSymbol", symbol);

        return map;
    }

    @Cacheable("finnHubQuotes")
    public Map getQuotes(List<String> symbols) {
        Map quotes = new HashMap();

        symbols.forEach(
            symbol -> {
                Map response = (Map) restTemplate
                    .getForEntity(String.format("%squote?token=%s&symbol=%s", finnHubApiUrl, finnHubApiKey, symbol), Map.class)
                    .getBody();

                response.put("tickerSymbol", symbol);

                quotes.put(symbol, response);
            }
        );

        return quotes;
    }

    // helper function for getChart function
    public Long getPastTimeUnix(Long currentTime, String range) {
        Integer daySeconds = 86400;
        switch (range) {
            case "D":
                currentTime -= daySeconds;
            case "W":
                currentTime -= daySeconds * 7;
            case "M":
                currentTime -= 2629743; // 30.44 days
            case "Y":
                currentTime -= 31556926; // 365.24 days
        }

        return currentTime;
    }

    // helper function for getChart function
    public List<String> convertUnixTimeToDate(List<Integer> unixTimes) {
        List<String> formattedDates = new ArrayList<String>();

        Instant date;
        String formattedDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-LL-d HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC));
        for (Integer time : unixTimes) {
            date = Instant.ofEpochSecond(time);
            formattedDate = formatter.format(date);
            formattedDates.add(formattedDate);
        }

        return formattedDates;
    }

    @Cacheable("finnHubChart")
    public List<Map> getChart(String symbol, String range) {
        Long currentTime = Instant.now().getEpochSecond();
        Long pastTime = getPastTimeUnix(currentTime, range);

        Map map = (Map) restTemplate
            .getForEntity(
                String.format(
                    "%sstock/candle?symbol=%s&resolution=D&from=%s&to=%s&token=%s",
                    finnHubApiUrl,
                    symbol,
                    pastTime,
                    currentTime,
                    finnHubApiKey
                ),
                Map.class
            )
            .getBody();

        List<Integer> unixTimes = (ArrayList<Integer>) map.get("t");
        List<String> formattedDates = convertUnixTimeToDate(unixTimes);

        map.replace("t", formattedDates);

        List<Double> openPrices = (List<Double>) map.get("o");
        List<Double> closePrices = (List<Double>) map.get("c");
        List<Double> highPrices = (List<Double>) map.get("h");
        List<Double> lowPrices = (List<Double>) map.get("l");
        List<Long> volumes = (List<Long>) map.get("v");
        List<Map> stockData = new ArrayList<Map>();

        Map stockDayData = new HashMap();
        for (int i = 0; i < formattedDates.size() - 1; i++) {
            stockDayData.put("time", formattedDates.get(i));
            stockDayData.put("open", openPrices.get(i));
            stockDayData.put("close", closePrices.get(i));
            stockDayData.put("high", highPrices.get(i));
            stockDayData.put("low", lowPrices.get(i));
            stockDayData.put("volume", volumes.get(i));

            stockData.add(stockDayData);
            stockDayData = new HashMap();
        }

        return stockData;
    }
}
