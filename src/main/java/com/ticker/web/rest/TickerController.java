package com.ticker.web.rest;

import com.ticker.domain.Mappers.StockMapper;
import com.ticker.service.FinnHubService;
import com.ticker.service.YahooFinanceService;
//import com.ticker.web.websocket.FinnHubWebSocketService;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import liquibase.util.csv.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class TickerController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    FinnHubService finnHubService;

    @Autowired
    YahooFinanceService yahooFinanceService;

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

    // helper function for getSymbols
    public boolean getCondition(boolean getAll, int iterations, int size) {
        boolean condition;

        if (getAll) condition = true; else condition = iterations < size;

        return condition;
    }

    /**
     * Returns list of symbols according to size given or available size
     **/
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
