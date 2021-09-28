package com.ticker.domain.Mappers;

/**
 * Mapper for finnhub and/or yfinance stock data
 */

import java.util.HashMap;
import java.util.Map;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

public class StockMapper {

    public static Map mergeStockData(Stock yfStock, Map fhStock) {
        Map map = new HashMap();
        StockQuote yfQuote = yfStock.getQuote();
        StockStats yfStats = yfStock.getStats();
        map.put("tickerSymbol", fhStock.get("tickerSymbol"));
        map.put("name", yfStock.getName());
        map.put("price", fhStock.get("c"));
        map.put("change", fhStock.get("d"));
        map.put("percentChange", fhStock.get("dp"));
        map.put("open", fhStock.get("o"));
        map.put("previousClose", fhStock.get("pc"));
        map.put("dayLow", fhStock.get("l"));
        map.put("dayHigh", fhStock.get("h"));
        map.put("yearLow", yfQuote.getYearLow());
        map.put("yearHigh", yfQuote.getYearHigh());
        map.put("volume", yfQuote.getVolume());
        map.put("avgVolume", yfQuote.getAvgVolume());
        map.put("marketCap", yfStats.getMarketCap());
        map.put("eps", yfStats.getEps());
        map.put("pe", yfStats.getPeg());
        map.put("ebitda", yfStats.getEBITDA());
        map.put("oneYearTargetPrice", yfStats.getOneYearTargetPrice());

        return map;
    }

    public static Map mergeStockData(Map fhStock) {
        Map map = new HashMap();
        map.put("tickerSymbol", fhStock.get("tickerSymbol"));
        map.put("price", fhStock.get("c"));
        map.put("change", fhStock.get("d"));
        map.put("percentChange", fhStock.get("dp"));
        map.put("open", fhStock.get("o"));
        map.put("previousClose", fhStock.get("pc"));
        map.put("dayLow", fhStock.get("l"));
        map.put("dayHigh", fhStock.get("h"));

        return map;
    }
    //    public static List<Map> mergeStockData(List<Map> fhStocks) {
    //        Map map = new HashMap();
    //        fhStocks.forEach(stock -> {
    //            map.putAll(mergeStockData(stock));
    //        });
    //
    //        return map;
    //    }

    //    public static List<Map> mergeStockData(Stock yfStocks, Map fhStocks) {
    //        Map map = new HashMap();
    //        fhStocks.forEach(stock -> {
    //            map.putAll(mergeStockData(stock));
    //        });
    //
    //        return map;
    //    }
}
