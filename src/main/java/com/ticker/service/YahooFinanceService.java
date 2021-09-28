package com.ticker.service;

/**
 * Service for the yfinance API.
 */

import com.ticker.domain.Mappers.StockMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@Service
public class YahooFinanceService {

    @Cacheable("yahooFinanceQuote")
    public Map getQuote(String symbol, Map response) throws IOException {
        Stock stock = YahooFinance.get(symbol);
        response = StockMapper.mergeStockData(stock, response);

        return response;
    }

    @Cacheable("yahooFinanceQuotes")
    public Map getQuotes(List<String> symbols, Map responses) throws IOException {
        Map stocks = (Map) YahooFinance.get(symbols.toArray(new String[0]));
        stocks.forEach(
            (symbol, stock) -> {
                stocks.put(symbol, StockMapper.mergeStockData((Stock) stock, (Map) responses.get(symbol)));
            }
        );

        return stocks;
    }
}
