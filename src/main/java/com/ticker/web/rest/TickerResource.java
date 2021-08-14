package com.ticker.web.rest;

import com.ticker.domain.Ticker;
import com.ticker.repository.TickerRepository;
import com.ticker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ticker.domain.Ticker}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TickerResource {

    private final Logger log = LoggerFactory.getLogger(TickerResource.class);

    private static final String ENTITY_NAME = "ticker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TickerRepository tickerRepository;

    public TickerResource(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    /**
     * {@code POST  /tickers} : Create a new ticker.
     *
     * @param ticker the ticker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticker, or with status {@code 400 (Bad Request)} if the ticker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickers")
    public ResponseEntity<Ticker> createTicker(@Valid @RequestBody Ticker ticker) throws URISyntaxException {
        log.debug("REST request to save Ticker : {}", ticker);
        if (ticker.getId() != null) {
            throw new BadRequestAlertException("A new ticker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ticker result = tickerRepository.save(ticker);
        return ResponseEntity
            .created(new URI("/api/tickers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickers/:id} : Updates an existing ticker.
     *
     * @param id the id of the ticker to save.
     * @param ticker the ticker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticker,
     * or with status {@code 400 (Bad Request)} if the ticker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickers/{id}")
    public ResponseEntity<Ticker> updateTicker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ticker ticker
    ) throws URISyntaxException {
        log.debug("REST request to update Ticker : {}, {}", id, ticker);
        if (ticker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ticker result = tickerRepository.save(ticker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticker.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickers/:id} : Partial updates given fields of an existing ticker, field will ignore if it is null
     *
     * @param id the id of the ticker to save.
     * @param ticker the ticker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticker,
     * or with status {@code 400 (Bad Request)} if the ticker is not valid,
     * or with status {@code 404 (Not Found)} if the ticker is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tickers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Ticker> partialUpdateTicker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ticker ticker
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ticker partially : {}, {}", id, ticker);
        if (ticker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ticker> result = tickerRepository
            .findById(ticker.getId())
            .map(
                existingTicker -> {
                    if (ticker.getSymbol() != null) {
                        existingTicker.setSymbol(ticker.getSymbol());
                    }
                    if (ticker.getUpdatedAt() != null) {
                        existingTicker.setUpdatedAt(ticker.getUpdatedAt());
                    }
                    if (ticker.getPriceChange() != null) {
                        existingTicker.setPriceChange(ticker.getPriceChange());
                    }
                    if (ticker.getPricePercentChange() != null) {
                        existingTicker.setPricePercentChange(ticker.getPricePercentChange());
                    }
                    if (ticker.getMarketPrice() != null) {
                        existingTicker.setMarketPrice(ticker.getMarketPrice());
                    }
                    if (ticker.getMarketCap() != null) {
                        existingTicker.setMarketCap(ticker.getMarketCap());
                    }
                    if (ticker.getVolume() != null) {
                        existingTicker.setVolume(ticker.getVolume());
                    }
                    if (ticker.getAvgVolume() != null) {
                        existingTicker.setAvgVolume(ticker.getAvgVolume());
                    }
                    if (ticker.getLow() != null) {
                        existingTicker.setLow(ticker.getLow());
                    }
                    if (ticker.getHigh() != null) {
                        existingTicker.setHigh(ticker.getHigh());
                    }
                    if (ticker.getOpen() != null) {
                        existingTicker.setOpen(ticker.getOpen());
                    }
                    if (ticker.getClose() != null) {
                        existingTicker.setClose(ticker.getClose());
                    }
                    if (ticker.getPreviousClose() != null) {
                        existingTicker.setPreviousClose(ticker.getPreviousClose());
                    }
                    if (ticker.getBid() != null) {
                        existingTicker.setBid(ticker.getBid());
                    }
                    if (ticker.getAsk() != null) {
                        existingTicker.setAsk(ticker.getAsk());
                    }
                    if (ticker.getBidVol() != null) {
                        existingTicker.setBidVol(ticker.getBidVol());
                    }
                    if (ticker.getAskVol() != null) {
                        existingTicker.setAskVol(ticker.getAskVol());
                    }
                    if (ticker.getFiftyTwoWeekLow() != null) {
                        existingTicker.setFiftyTwoWeekLow(ticker.getFiftyTwoWeekLow());
                    }
                    if (ticker.getFiftyTwoWeekHigh() != null) {
                        existingTicker.setFiftyTwoWeekHigh(ticker.getFiftyTwoWeekHigh());
                    }
                    if (ticker.getBeta() != null) {
                        existingTicker.setBeta(ticker.getBeta());
                    }
                    if (ticker.getPeRatio() != null) {
                        existingTicker.setPeRatio(ticker.getPeRatio());
                    }
                    if (ticker.getEps() != null) {
                        existingTicker.setEps(ticker.getEps());
                    }

                    return existingTicker;
                }
            )
            .map(tickerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ticker.getId().toString())
        );
    }

    /**
     * {@code GET  /tickers} : get all the tickers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickers in body.
     */
    @GetMapping("/tickers")
    public List<Ticker> getAllTickers() {
        log.debug("REST request to get all Tickers");
        return tickerRepository.findAll();
    }

    /**
     * {@code GET  /tickers/:symbol} : get the "symbol" ticker by maximum date.
     *
     * @param symbol the symbol of the ticker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickers/{symbol}")
    public ResponseEntity<Ticker> getTicker(@PathVariable String symbol) {
        log.debug("REST request to get Ticker : {}", symbol);
        Optional<Ticker> ticker = tickerRepository.findFirstBySymbolOrderByUpdatedAtDesc(symbol);
        return ResponseUtil.wrapOrNotFound(ticker);
    }

    /**
     * {@code DELETE  /tickers/:id} : delete the "id" ticker.
     *
     * @param id the id of the ticker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickers/{id}")
    public ResponseEntity<Void> deleteTicker(@PathVariable Long id) {
        log.debug("REST request to delete Ticker : {}", id);
        tickerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
