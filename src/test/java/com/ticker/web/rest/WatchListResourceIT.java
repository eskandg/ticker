package com.ticker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ticker.IntegrationTest;
import com.ticker.domain.WatchList;
import com.ticker.repository.WatchListRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WatchListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WatchListResourceIT {

    private static final String ENTITY_API_URL = "/api/watch-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WatchListRepository watchListRepository;

    @Mock
    private WatchListRepository watchListRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWatchListMockMvc;

    private WatchList watchList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchList createEntity(EntityManager em) {
        WatchList watchList = new WatchList();
        return watchList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchList createUpdatedEntity(EntityManager em) {
        WatchList watchList = new WatchList();
        return watchList;
    }

    @BeforeEach
    public void initTest() {
        watchList = createEntity(em);
    }

    @Test
    @Transactional
    void createWatchList() throws Exception {
        int databaseSizeBeforeCreate = watchListRepository.findAll().size();
        // Create the WatchList
        restWatchListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchList)))
            .andExpect(status().isCreated());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeCreate + 1);
        WatchList testWatchList = watchListList.get(watchListList.size() - 1);
    }

    @Test
    @Transactional
    void createWatchListWithExistingId() throws Exception {
        // Create the WatchList with an existing ID
        watchList.setId(1L);

        int databaseSizeBeforeCreate = watchListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchList)))
            .andExpect(status().isBadRequest());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWatchLists() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        // Get all the watchListList
        restWatchListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchList.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(watchListRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWatchListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(watchListRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(watchListRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWatchListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(watchListRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWatchList() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        // Get the watchList
        restWatchListMockMvc
            .perform(get(ENTITY_API_URL_ID, watchList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(watchList.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingWatchList() throws Exception {
        // Get the watchList
        restWatchListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWatchList() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();

        // Update the watchList
        WatchList updatedWatchList = watchListRepository.findById(watchList.getId()).get();
        // Disconnect from session so that the updates on updatedWatchList are not directly saved in db
        em.detach(updatedWatchList);

        restWatchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWatchList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWatchList))
            )
            .andExpect(status().isOk());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
        WatchList testWatchList = watchListList.get(watchListList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watchList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(watchList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(watchList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWatchListWithPatch() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();

        // Update the watchList using partial update
        WatchList partialUpdatedWatchList = new WatchList();
        partialUpdatedWatchList.setId(watchList.getId());

        restWatchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWatchList))
            )
            .andExpect(status().isOk());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
        WatchList testWatchList = watchListList.get(watchListList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateWatchListWithPatch() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();

        // Update the watchList using partial update
        WatchList partialUpdatedWatchList = new WatchList();
        partialUpdatedWatchList.setId(watchList.getId());

        restWatchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWatchList))
            )
            .andExpect(status().isOk());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
        WatchList testWatchList = watchListList.get(watchListList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, watchList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(watchList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(watchList))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWatchList() throws Exception {
        int databaseSizeBeforeUpdate = watchListRepository.findAll().size();
        watchList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(watchList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchList in the database
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWatchList() throws Exception {
        // Initialize the database
        watchListRepository.saveAndFlush(watchList);

        int databaseSizeBeforeDelete = watchListRepository.findAll().size();

        // Delete the watchList
        restWatchListMockMvc
            .perform(delete(ENTITY_API_URL_ID, watchList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WatchList> watchListList = watchListRepository.findAll();
        assertThat(watchListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
