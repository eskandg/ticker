package com.ticker.service;

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
}
