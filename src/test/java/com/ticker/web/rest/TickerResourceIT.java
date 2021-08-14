package com.ticker.web.rest;

import static com.ticker.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ticker.IntegrationTest;
import com.ticker.domain.Ticker;
import com.ticker.repository.TickerRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TickerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TickerResourceIT {

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PRICE_CHANGE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_CHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE_PERCENT_CHANGE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_PERCENT_CHANGE = "BBBBBBBBBB";

    private static final Double DEFAULT_MARKET_PRICE = 1D;
    private static final Double UPDATED_MARKET_PRICE = 2D;

    private static final String DEFAULT_MARKET_CAP = "AAAAAAAAAA";
    private static final String UPDATED_MARKET_CAP = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    private static final Integer DEFAULT_AVG_VOLUME = 1;
    private static final Integer UPDATED_AVG_VOLUME = 2;

    private static final Double DEFAULT_LOW = 1D;
    private static final Double UPDATED_LOW = 2D;

    private static final Double DEFAULT_HIGH = 1D;
    private static final Double UPDATED_HIGH = 2D;

    private static final Double DEFAULT_OPEN = 1D;
    private static final Double UPDATED_OPEN = 2D;

    private static final Double DEFAULT_CLOSE = 1D;
    private static final Double UPDATED_CLOSE = 2D;

    private static final Double DEFAULT_PREVIOUS_CLOSE = 1D;
    private static final Double UPDATED_PREVIOUS_CLOSE = 2D;

    private static final Double DEFAULT_BID = 1D;
    private static final Double UPDATED_BID = 2D;

    private static final Double DEFAULT_ASK = 1D;
    private static final Double UPDATED_ASK = 2D;

    private static final Double DEFAULT_BID_VOL = 1D;
    private static final Double UPDATED_BID_VOL = 2D;

    private static final Double DEFAULT_ASK_VOL = 1D;
    private static final Double UPDATED_ASK_VOL = 2D;

    private static final Double DEFAULT_FIFTY_TWO_WEEK_LOW = 1D;
    private static final Double UPDATED_FIFTY_TWO_WEEK_LOW = 2D;

    private static final Double DEFAULT_FIFTY_TWO_WEEK_HIGH = 1D;
    private static final Double UPDATED_FIFTY_TWO_WEEK_HIGH = 2D;

    private static final Float DEFAULT_BETA = 1F;
    private static final Float UPDATED_BETA = 2F;

    private static final Float DEFAULT_PE_RATIO = 1F;
    private static final Float UPDATED_PE_RATIO = 2F;

    private static final Float DEFAULT_EPS = 1F;
    private static final Float UPDATED_EPS = 2F;

    private static final String ENTITY_API_URL = "/api/tickers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickerMockMvc;

    private Ticker ticker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .symbol(DEFAULT_SYMBOL)
            .updatedAt(DEFAULT_UPDATED_AT)
            .priceChange(DEFAULT_PRICE_CHANGE)
            .pricePercentChange(DEFAULT_PRICE_PERCENT_CHANGE)
            .marketPrice(DEFAULT_MARKET_PRICE)
            .marketCap(DEFAULT_MARKET_CAP)
            .volume(DEFAULT_VOLUME)
            .avgVolume(DEFAULT_AVG_VOLUME)
            .low(DEFAULT_LOW)
            .high(DEFAULT_HIGH)
            .open(DEFAULT_OPEN)
            .close(DEFAULT_CLOSE)
            .previousClose(DEFAULT_PREVIOUS_CLOSE)
            .bid(DEFAULT_BID)
            .ask(DEFAULT_ASK)
            .bidVol(DEFAULT_BID_VOL)
            .askVol(DEFAULT_ASK_VOL)
            .fiftyTwoWeekLow(DEFAULT_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(DEFAULT_FIFTY_TWO_WEEK_HIGH)
            .beta(DEFAULT_BETA)
            .peRatio(DEFAULT_PE_RATIO)
            .eps(DEFAULT_EPS);
        return ticker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticker createUpdatedEntity(EntityManager em) {
        Ticker ticker = new Ticker()
            .symbol(UPDATED_SYMBOL)
            .updatedAt(UPDATED_UPDATED_AT)
            .priceChange(UPDATED_PRICE_CHANGE)
            .pricePercentChange(UPDATED_PRICE_PERCENT_CHANGE)
            .marketPrice(UPDATED_MARKET_PRICE)
            .marketCap(UPDATED_MARKET_CAP)
            .volume(UPDATED_VOLUME)
            .avgVolume(UPDATED_AVG_VOLUME)
            .low(UPDATED_LOW)
            .high(UPDATED_HIGH)
            .open(UPDATED_OPEN)
            .close(UPDATED_CLOSE)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .bid(UPDATED_BID)
            .ask(UPDATED_ASK)
            .bidVol(UPDATED_BID_VOL)
            .askVol(UPDATED_ASK_VOL)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .beta(UPDATED_BETA)
            .peRatio(UPDATED_PE_RATIO)
            .eps(UPDATED_EPS);
        return ticker;
    }

    @BeforeEach
    public void initTest() {
        ticker = createEntity(em);
    }

    @Test
    @Transactional
    void createTicker() throws Exception {
        int databaseSizeBeforeCreate = tickerRepository.findAll().size();
        // Create the Ticker
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticker)))
            .andExpect(status().isCreated());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate + 1);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testTicker.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTicker.getPriceChange()).isEqualTo(DEFAULT_PRICE_CHANGE);
        assertThat(testTicker.getPricePercentChange()).isEqualTo(DEFAULT_PRICE_PERCENT_CHANGE);
        assertThat(testTicker.getMarketPrice()).isEqualTo(DEFAULT_MARKET_PRICE);
        assertThat(testTicker.getMarketCap()).isEqualTo(DEFAULT_MARKET_CAP);
        assertThat(testTicker.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testTicker.getAvgVolume()).isEqualTo(DEFAULT_AVG_VOLUME);
        assertThat(testTicker.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testTicker.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testTicker.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testTicker.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testTicker.getPreviousClose()).isEqualTo(DEFAULT_PREVIOUS_CLOSE);
        assertThat(testTicker.getBid()).isEqualTo(DEFAULT_BID);
        assertThat(testTicker.getAsk()).isEqualTo(DEFAULT_ASK);
        assertThat(testTicker.getBidVol()).isEqualTo(DEFAULT_BID_VOL);
        assertThat(testTicker.getAskVol()).isEqualTo(DEFAULT_ASK_VOL);
        assertThat(testTicker.getFiftyTwoWeekLow()).isEqualTo(DEFAULT_FIFTY_TWO_WEEK_LOW);
        assertThat(testTicker.getFiftyTwoWeekHigh()).isEqualTo(DEFAULT_FIFTY_TWO_WEEK_HIGH);
        assertThat(testTicker.getBeta()).isEqualTo(DEFAULT_BETA);
        assertThat(testTicker.getPeRatio()).isEqualTo(DEFAULT_PE_RATIO);
        assertThat(testTicker.getEps()).isEqualTo(DEFAULT_EPS);
    }

    @Test
    @Transactional
    void createTickerWithExistingId() throws Exception {
        // Create the Ticker with an existing ID
        ticker.setId(1L);

        int databaseSizeBeforeCreate = tickerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticker)))
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = tickerRepository.findAll().size();
        // set the field null
        ticker.setSymbol(null);

        // Create the Ticker, which fails.

        restTickerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticker)))
            .andExpect(status().isBadRequest());

        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickers() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get all the tickerList
        restTickerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticker.getId().intValue())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].priceChange").value(hasItem(DEFAULT_PRICE_CHANGE)))
            .andExpect(jsonPath("$.[*].pricePercentChange").value(hasItem(DEFAULT_PRICE_PERCENT_CHANGE)))
            .andExpect(jsonPath("$.[*].marketPrice").value(hasItem(DEFAULT_MARKET_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketCap").value(hasItem(DEFAULT_MARKET_CAP)))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)))
            .andExpect(jsonPath("$.[*].avgVolume").value(hasItem(DEFAULT_AVG_VOLUME)))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
            .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.doubleValue())))
            .andExpect(jsonPath("$.[*].previousClose").value(hasItem(DEFAULT_PREVIOUS_CLOSE.doubleValue())))
            .andExpect(jsonPath("$.[*].bid").value(hasItem(DEFAULT_BID.doubleValue())))
            .andExpect(jsonPath("$.[*].ask").value(hasItem(DEFAULT_ASK.doubleValue())))
            .andExpect(jsonPath("$.[*].bidVol").value(hasItem(DEFAULT_BID_VOL.doubleValue())))
            .andExpect(jsonPath("$.[*].askVol").value(hasItem(DEFAULT_ASK_VOL.doubleValue())))
            .andExpect(jsonPath("$.[*].fiftyTwoWeekLow").value(hasItem(DEFAULT_FIFTY_TWO_WEEK_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].fiftyTwoWeekHigh").value(hasItem(DEFAULT_FIFTY_TWO_WEEK_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].beta").value(hasItem(DEFAULT_BETA.doubleValue())))
            .andExpect(jsonPath("$.[*].peRatio").value(hasItem(DEFAULT_PE_RATIO.doubleValue())))
            .andExpect(jsonPath("$.[*].eps").value(hasItem(DEFAULT_EPS.doubleValue())));
    }

    @Test
    @Transactional
    void getTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        // Get the ticker
        restTickerMockMvc
            .perform(get(ENTITY_API_URL_ID, ticker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticker.getId().intValue()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.priceChange").value(DEFAULT_PRICE_CHANGE))
            .andExpect(jsonPath("$.pricePercentChange").value(DEFAULT_PRICE_PERCENT_CHANGE))
            .andExpect(jsonPath("$.marketPrice").value(DEFAULT_MARKET_PRICE.doubleValue()))
            .andExpect(jsonPath("$.marketCap").value(DEFAULT_MARKET_CAP))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME))
            .andExpect(jsonPath("$.avgVolume").value(DEFAULT_AVG_VOLUME))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.doubleValue()))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.previousClose").value(DEFAULT_PREVIOUS_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.bid").value(DEFAULT_BID.doubleValue()))
            .andExpect(jsonPath("$.ask").value(DEFAULT_ASK.doubleValue()))
            .andExpect(jsonPath("$.bidVol").value(DEFAULT_BID_VOL.doubleValue()))
            .andExpect(jsonPath("$.askVol").value(DEFAULT_ASK_VOL.doubleValue()))
            .andExpect(jsonPath("$.fiftyTwoWeekLow").value(DEFAULT_FIFTY_TWO_WEEK_LOW.doubleValue()))
            .andExpect(jsonPath("$.fiftyTwoWeekHigh").value(DEFAULT_FIFTY_TWO_WEEK_HIGH.doubleValue()))
            .andExpect(jsonPath("$.beta").value(DEFAULT_BETA.doubleValue()))
            .andExpect(jsonPath("$.peRatio").value(DEFAULT_PE_RATIO.doubleValue()))
            .andExpect(jsonPath("$.eps").value(DEFAULT_EPS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTicker() throws Exception {
        // Get the ticker
        restTickerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker
        Ticker updatedTicker = tickerRepository.findById(ticker.getId()).get();
        // Disconnect from session so that the updates on updatedTicker are not directly saved in db
        em.detach(updatedTicker);
        updatedTicker
            .symbol(UPDATED_SYMBOL)
            .updatedAt(UPDATED_UPDATED_AT)
            .priceChange(UPDATED_PRICE_CHANGE)
            .pricePercentChange(UPDATED_PRICE_PERCENT_CHANGE)
            .marketPrice(UPDATED_MARKET_PRICE)
            .marketCap(UPDATED_MARKET_CAP)
            .volume(UPDATED_VOLUME)
            .avgVolume(UPDATED_AVG_VOLUME)
            .low(UPDATED_LOW)
            .high(UPDATED_HIGH)
            .open(UPDATED_OPEN)
            .close(UPDATED_CLOSE)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .bid(UPDATED_BID)
            .ask(UPDATED_ASK)
            .bidVol(UPDATED_BID_VOL)
            .askVol(UPDATED_ASK_VOL)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .beta(UPDATED_BETA)
            .peRatio(UPDATED_PE_RATIO)
            .eps(UPDATED_EPS);

        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTicker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTicker.getPriceChange()).isEqualTo(UPDATED_PRICE_CHANGE);
        assertThat(testTicker.getPricePercentChange()).isEqualTo(UPDATED_PRICE_PERCENT_CHANGE);
        assertThat(testTicker.getMarketPrice()).isEqualTo(UPDATED_MARKET_PRICE);
        assertThat(testTicker.getMarketCap()).isEqualTo(UPDATED_MARKET_CAP);
        assertThat(testTicker.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testTicker.getAvgVolume()).isEqualTo(UPDATED_AVG_VOLUME);
        assertThat(testTicker.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testTicker.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTicker.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testTicker.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testTicker.getPreviousClose()).isEqualTo(UPDATED_PREVIOUS_CLOSE);
        assertThat(testTicker.getBid()).isEqualTo(UPDATED_BID);
        assertThat(testTicker.getAsk()).isEqualTo(UPDATED_ASK);
        assertThat(testTicker.getBidVol()).isEqualTo(UPDATED_BID_VOL);
        assertThat(testTicker.getAskVol()).isEqualTo(UPDATED_ASK_VOL);
        assertThat(testTicker.getFiftyTwoWeekLow()).isEqualTo(UPDATED_FIFTY_TWO_WEEK_LOW);
        assertThat(testTicker.getFiftyTwoWeekHigh()).isEqualTo(UPDATED_FIFTY_TWO_WEEK_HIGH);
        assertThat(testTicker.getBeta()).isEqualTo(UPDATED_BETA);
        assertThat(testTicker.getPeRatio()).isEqualTo(UPDATED_PE_RATIO);
        assertThat(testTicker.getEps()).isEqualTo(UPDATED_EPS);
    }

    @Test
    @Transactional
    void putNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker
            .symbol(UPDATED_SYMBOL)
            .priceChange(UPDATED_PRICE_CHANGE)
            .pricePercentChange(UPDATED_PRICE_PERCENT_CHANGE)
            .avgVolume(UPDATED_AVG_VOLUME)
            .high(UPDATED_HIGH)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .bid(UPDATED_BID)
            .ask(UPDATED_ASK)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .beta(UPDATED_BETA)
            .eps(UPDATED_EPS);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTicker.getPriceChange()).isEqualTo(UPDATED_PRICE_CHANGE);
        assertThat(testTicker.getPricePercentChange()).isEqualTo(UPDATED_PRICE_PERCENT_CHANGE);
        assertThat(testTicker.getMarketPrice()).isEqualTo(DEFAULT_MARKET_PRICE);
        assertThat(testTicker.getMarketCap()).isEqualTo(DEFAULT_MARKET_CAP);
        assertThat(testTicker.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testTicker.getAvgVolume()).isEqualTo(UPDATED_AVG_VOLUME);
        assertThat(testTicker.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testTicker.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTicker.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testTicker.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testTicker.getPreviousClose()).isEqualTo(UPDATED_PREVIOUS_CLOSE);
        assertThat(testTicker.getBid()).isEqualTo(UPDATED_BID);
        assertThat(testTicker.getAsk()).isEqualTo(UPDATED_ASK);
        assertThat(testTicker.getBidVol()).isEqualTo(DEFAULT_BID_VOL);
        assertThat(testTicker.getAskVol()).isEqualTo(DEFAULT_ASK_VOL);
        assertThat(testTicker.getFiftyTwoWeekLow()).isEqualTo(DEFAULT_FIFTY_TWO_WEEK_LOW);
        assertThat(testTicker.getFiftyTwoWeekHigh()).isEqualTo(UPDATED_FIFTY_TWO_WEEK_HIGH);
        assertThat(testTicker.getBeta()).isEqualTo(UPDATED_BETA);
        assertThat(testTicker.getPeRatio()).isEqualTo(DEFAULT_PE_RATIO);
        assertThat(testTicker.getEps()).isEqualTo(UPDATED_EPS);
    }

    @Test
    @Transactional
    void fullUpdateTickerWithPatch() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();

        // Update the ticker using partial update
        Ticker partialUpdatedTicker = new Ticker();
        partialUpdatedTicker.setId(ticker.getId());

        partialUpdatedTicker
            .symbol(UPDATED_SYMBOL)
            .updatedAt(UPDATED_UPDATED_AT)
            .priceChange(UPDATED_PRICE_CHANGE)
            .pricePercentChange(UPDATED_PRICE_PERCENT_CHANGE)
            .marketPrice(UPDATED_MARKET_PRICE)
            .marketCap(UPDATED_MARKET_CAP)
            .volume(UPDATED_VOLUME)
            .avgVolume(UPDATED_AVG_VOLUME)
            .low(UPDATED_LOW)
            .high(UPDATED_HIGH)
            .open(UPDATED_OPEN)
            .close(UPDATED_CLOSE)
            .previousClose(UPDATED_PREVIOUS_CLOSE)
            .bid(UPDATED_BID)
            .ask(UPDATED_ASK)
            .bidVol(UPDATED_BID_VOL)
            .askVol(UPDATED_ASK_VOL)
            .fiftyTwoWeekLow(UPDATED_FIFTY_TWO_WEEK_LOW)
            .fiftyTwoWeekHigh(UPDATED_FIFTY_TWO_WEEK_HIGH)
            .beta(UPDATED_BETA)
            .peRatio(UPDATED_PE_RATIO)
            .eps(UPDATED_EPS);

        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicker))
            )
            .andExpect(status().isOk());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
        Ticker testTicker = tickerList.get(tickerList.size() - 1);
        assertThat(testTicker.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testTicker.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTicker.getPriceChange()).isEqualTo(UPDATED_PRICE_CHANGE);
        assertThat(testTicker.getPricePercentChange()).isEqualTo(UPDATED_PRICE_PERCENT_CHANGE);
        assertThat(testTicker.getMarketPrice()).isEqualTo(UPDATED_MARKET_PRICE);
        assertThat(testTicker.getMarketCap()).isEqualTo(UPDATED_MARKET_CAP);
        assertThat(testTicker.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testTicker.getAvgVolume()).isEqualTo(UPDATED_AVG_VOLUME);
        assertThat(testTicker.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testTicker.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testTicker.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testTicker.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testTicker.getPreviousClose()).isEqualTo(UPDATED_PREVIOUS_CLOSE);
        assertThat(testTicker.getBid()).isEqualTo(UPDATED_BID);
        assertThat(testTicker.getAsk()).isEqualTo(UPDATED_ASK);
        assertThat(testTicker.getBidVol()).isEqualTo(UPDATED_BID_VOL);
        assertThat(testTicker.getAskVol()).isEqualTo(UPDATED_ASK_VOL);
        assertThat(testTicker.getFiftyTwoWeekLow()).isEqualTo(UPDATED_FIFTY_TWO_WEEK_LOW);
        assertThat(testTicker.getFiftyTwoWeekHigh()).isEqualTo(UPDATED_FIFTY_TWO_WEEK_HIGH);
        assertThat(testTicker.getBeta()).isEqualTo(UPDATED_BETA);
        assertThat(testTicker.getPeRatio()).isEqualTo(UPDATED_PE_RATIO);
        assertThat(testTicker.getEps()).isEqualTo(UPDATED_EPS);
    }

    @Test
    @Transactional
    void patchNonExistingTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicker() throws Exception {
        int databaseSizeBeforeUpdate = tickerRepository.findAll().size();
        ticker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ticker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticker in the database
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicker() throws Exception {
        // Initialize the database
        tickerRepository.saveAndFlush(ticker);

        int databaseSizeBeforeDelete = tickerRepository.findAll().size();

        // Delete the ticker
        restTickerMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticker> tickerList = tickerRepository.findAll();
        assertThat(tickerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
