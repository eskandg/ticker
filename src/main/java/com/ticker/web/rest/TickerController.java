package com.ticker.web.rest;

import com.ticker.domain.Mappers.StockMapper;
import com.ticker.service.FinnHubService;
import com.ticker.service.YahooFinanceService;
//import com.ticker.web.websocket.FinnHubWebSocketService;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;
import liquibase.util.csv.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * REST controller for managing ticker data.
 */
@RestController
@RequestMapping("/api")
public class TickerController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    FinnHubService finnHubService;

    @Autowired
    YahooFinanceService yahooFinanceService;

    /**
     * {@code GET  /ticker} : Get data for a ticker.
     *
     * @param symbol the symbol of the ticker to get.
     * @param getMoreDetails boolean if true gets yahoo finance and finnhub ticker data else only gets finnhub data.
     * @return Map containing ticker data, dependent on getMoreDetails.
     */
    @GetMapping("/ticker")
    public ResponseEntity<Map> getTicker(@RequestParam String symbol, boolean getMoreDetails, boolean isSocketActive) throws IOException {
        Map response = finnHubService.getQuote(symbol);

        // every 5 minutes or when the timing starts, make a request to yahoo finance
        if (getMoreDetails) {
            response = yahooFinanceService.getQuote(symbol, response);
        } else {
            response = StockMapper.mergeStockData(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * {@code GET  /tickers} : Gets data for a list of tickers.
     *
     * @param symbols the symbols of tickers to get.
     * @param getMoreDetails boolean if true gets yahoo finance and finnhub ticker data else only gets finnhub data.
     * @return Map containing ticker data, dependent on getMoreDetails.
     */
    @GetMapping("/tickers")
    public ResponseEntity<Map> getTickers(@RequestParam List<String> symbols, boolean getMoreDetails, boolean isSocketActive)
        throws IOException {
        Map responses = finnHubService.getQuotes(symbols);

        if (getMoreDetails) responses = yahooFinanceService.getQuotes(symbols, responses); else {
            for (String symbol : symbols) {
                responses.replace(symbol, StockMapper.mergeStockData((Map) responses.get(symbol)));
            }
        }
        return ResponseEntity.ok(responses);
    }

    /**
     * {@code GET  /chart} : Get chart data for a ticker.
     *
     * @param symbol the symbol of the ticker chart to get.
     * @param range the time ranging between a day and a year in the past (for example "Y" is a year)
     * @return List of maps containing chart data, dependent on range.
     */
    @GetMapping("/chart")
    public ResponseEntity<List<Map>> getChart(@RequestParam String symbol, String range) {
        List<Map> response = finnHubService.getChart(symbol, range);
        return ResponseEntity.ok(response);
    }

    // helper function for getSymbols
    public boolean getCondition(boolean getAll, int iterations, int size) {
        boolean condition;

        if (getAll) condition = true; else condition = iterations < size;

        return condition;
    }

    /**
     * {@code GET  /symbols} : Gets a number of or all symbols.
     *
     * @param getAll gets all the symbols if true.
     * @param pageable the pagination information.
     * @return Returns list of symbols according to size given or available size.
     */
    @GetMapping("/symbols")
    public List<String> getSymbols(boolean getAll, Pageable pageable) throws FileNotFoundException {
        final String file = Paths.get("src/main/java/com/ticker/web/rest").toAbsolutePath() + "\\tickers.csv";

        String[] line;
        List<String> symbols = new ArrayList<String>();

        try {
            CSVReader reader = new CSVReader(new FileReader(file));

            int iterations = 0;
            boolean skippedHeader = false;

            while (getCondition(getAll, iterations, pageable.getPageSize()) && (line = reader.readNext()) != null) {
                if (!skippedHeader) skippedHeader = true; else {
                    symbols.addAll(Arrays.asList(line));
                    iterations++;
                }
            }
            //            final Page<List<String>> page = Pag
            //            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return symbols;
    }
}
