package com.ticker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ticker.IntegrationTest;
import com.ticker.domain.TickerProfile;
import com.ticker.repository.TickerProfileRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TickerProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TickerProfileResourceIT {

    private static final String DEFAULT_TICKER_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_TICKER_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_FULL_TIME_EMPLOYEES = 1;
    private static final Integer UPDATED_FULL_TIME_EMPLOYEES = 2;

    private static final String ENTITY_API_URL = "/api/ticker-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TickerProfileRepository tickerProfileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTickerProfileMockMvc;

    private TickerProfile tickerProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TickerProfile createEntity(EntityManager em) {
        TickerProfile tickerProfile = new TickerProfile()
            .tickerSymbol(DEFAULT_TICKER_SYMBOL)
            .logoUrl(DEFAULT_LOGO_URL)
            .industry(DEFAULT_INDUSTRY)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .website(DEFAULT_WEBSITE)
            .description(DEFAULT_DESCRIPTION)
            .fullTimeEmployees(DEFAULT_FULL_TIME_EMPLOYEES);
        return tickerProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TickerProfile createUpdatedEntity(EntityManager em) {
        TickerProfile tickerProfile = new TickerProfile()
            .tickerSymbol(UPDATED_TICKER_SYMBOL)
            .logoUrl(UPDATED_LOGO_URL)
            .industry(UPDATED_INDUSTRY)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .fullTimeEmployees(UPDATED_FULL_TIME_EMPLOYEES);
        return tickerProfile;
    }

    @BeforeEach
    public void initTest() {
        tickerProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createTickerProfile() throws Exception {
        int databaseSizeBeforeCreate = tickerProfileRepository.findAll().size();
        // Create the TickerProfile
        restTickerProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerProfile)))
            .andExpect(status().isCreated());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        TickerProfile testTickerProfile = tickerProfileList.get(tickerProfileList.size() - 1);
        assertThat(testTickerProfile.getTickerSymbol()).isEqualTo(DEFAULT_TICKER_SYMBOL);
        assertThat(testTickerProfile.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testTickerProfile.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testTickerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTickerProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTickerProfile.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testTickerProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTickerProfile.getFullTimeEmployees()).isEqualTo(DEFAULT_FULL_TIME_EMPLOYEES);
    }

    @Test
    @Transactional
    void createTickerProfileWithExistingId() throws Exception {
        // Create the TickerProfile with an existing ID
        tickerProfile.setId(1L);

        int databaseSizeBeforeCreate = tickerProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTickerProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerProfile)))
            .andExpect(status().isBadRequest());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTickerSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = tickerProfileRepository.findAll().size();
        // set the field null
        tickerProfile.setTickerSymbol(null);

        // Create the TickerProfile, which fails.

        restTickerProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerProfile)))
            .andExpect(status().isBadRequest());

        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickerProfiles() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        // Get all the tickerProfileList
        restTickerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tickerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].tickerSymbol").value(hasItem(DEFAULT_TICKER_SYMBOL)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fullTimeEmployees").value(hasItem(DEFAULT_FULL_TIME_EMPLOYEES)));
    }

    @Test
    @Transactional
    void getTickerProfile() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        // Get the tickerProfile
        restTickerProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, tickerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tickerProfile.getId().intValue()))
            .andExpect(jsonPath("$.tickerSymbol").value(DEFAULT_TICKER_SYMBOL))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fullTimeEmployees").value(DEFAULT_FULL_TIME_EMPLOYEES));
    }

    @Test
    @Transactional
    void getNonExistingTickerProfile() throws Exception {
        // Get the tickerProfile
        restTickerProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTickerProfile() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();

        // Update the tickerProfile
        TickerProfile updatedTickerProfile = tickerProfileRepository.findById(tickerProfile.getId()).get();
        // Disconnect from session so that the updates on updatedTickerProfile are not directly saved in db
        em.detach(updatedTickerProfile);
        updatedTickerProfile
            .tickerSymbol(UPDATED_TICKER_SYMBOL)
            .logoUrl(UPDATED_LOGO_URL)
            .industry(UPDATED_INDUSTRY)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .fullTimeEmployees(UPDATED_FULL_TIME_EMPLOYEES);

        restTickerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTickerProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTickerProfile))
            )
            .andExpect(status().isOk());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
        TickerProfile testTickerProfile = tickerProfileList.get(tickerProfileList.size() - 1);
        assertThat(testTickerProfile.getTickerSymbol()).isEqualTo(UPDATED_TICKER_SYMBOL);
        assertThat(testTickerProfile.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testTickerProfile.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testTickerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTickerProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTickerProfile.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTickerProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTickerProfile.getFullTimeEmployees()).isEqualTo(UPDATED_FULL_TIME_EMPLOYEES);
    }

    @Test
    @Transactional
    void putNonExistingTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tickerProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tickerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tickerProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTickerProfileWithPatch() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();

        // Update the tickerProfile using partial update
        TickerProfile partialUpdatedTickerProfile = new TickerProfile();
        partialUpdatedTickerProfile.setId(tickerProfile.getId());

        partialUpdatedTickerProfile
            .tickerSymbol(UPDATED_TICKER_SYMBOL)
            .industry(UPDATED_INDUSTRY)
            .fullTimeEmployees(UPDATED_FULL_TIME_EMPLOYEES);

        restTickerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickerProfile))
            )
            .andExpect(status().isOk());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
        TickerProfile testTickerProfile = tickerProfileList.get(tickerProfileList.size() - 1);
        assertThat(testTickerProfile.getTickerSymbol()).isEqualTo(UPDATED_TICKER_SYMBOL);
        assertThat(testTickerProfile.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testTickerProfile.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testTickerProfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTickerProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTickerProfile.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testTickerProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTickerProfile.getFullTimeEmployees()).isEqualTo(UPDATED_FULL_TIME_EMPLOYEES);
    }

    @Test
    @Transactional
    void fullUpdateTickerProfileWithPatch() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();

        // Update the tickerProfile using partial update
        TickerProfile partialUpdatedTickerProfile = new TickerProfile();
        partialUpdatedTickerProfile.setId(tickerProfile.getId());

        partialUpdatedTickerProfile
            .tickerSymbol(UPDATED_TICKER_SYMBOL)
            .logoUrl(UPDATED_LOGO_URL)
            .industry(UPDATED_INDUSTRY)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .fullTimeEmployees(UPDATED_FULL_TIME_EMPLOYEES);

        restTickerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTickerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTickerProfile))
            )
            .andExpect(status().isOk());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
        TickerProfile testTickerProfile = tickerProfileList.get(tickerProfileList.size() - 1);
        assertThat(testTickerProfile.getTickerSymbol()).isEqualTo(UPDATED_TICKER_SYMBOL);
        assertThat(testTickerProfile.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testTickerProfile.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testTickerProfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTickerProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTickerProfile.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testTickerProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTickerProfile.getFullTimeEmployees()).isEqualTo(UPDATED_FULL_TIME_EMPLOYEES);
    }

    @Test
    @Transactional
    void patchNonExistingTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tickerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tickerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTickerProfile() throws Exception {
        int databaseSizeBeforeUpdate = tickerProfileRepository.findAll().size();
        tickerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTickerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tickerProfile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TickerProfile in the database
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTickerProfile() throws Exception {
        // Initialize the database
        tickerProfileRepository.saveAndFlush(tickerProfile);

        int databaseSizeBeforeDelete = tickerProfileRepository.findAll().size();

        // Delete the tickerProfile
        restTickerProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, tickerProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TickerProfile> tickerProfileList = tickerProfileRepository.findAll();
        assertThat(tickerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
