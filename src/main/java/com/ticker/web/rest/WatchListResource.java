package com.ticker.web.rest;

import com.ticker.domain.WatchList;
import com.ticker.repository.WatchListRepository;
import com.ticker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ticker.domain.WatchList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WatchListResource {

    private final Logger log = LoggerFactory.getLogger(WatchListResource.class);

    private static final String ENTITY_NAME = "watchList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WatchListRepository watchListRepository;

    public WatchListResource(WatchListRepository watchListRepository) {
        this.watchListRepository = watchListRepository;
    }

    /**
     * {@code POST  /watch-lists} : Create a new watchList.
     *
     * @param watchList the watchList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new watchList, or with status {@code 400 (Bad Request)} if the watchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/watch-lists")
    public ResponseEntity<WatchList> createWatchList(@RequestBody WatchList watchList) throws URISyntaxException {
        log.debug("REST request to save WatchList : {}", watchList);
        if (watchList.getId() != null) {
            throw new BadRequestAlertException("A new watchList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WatchList result = watchListRepository.save(watchList);
        return ResponseEntity
            .created(new URI("/api/watch-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /watch-lists/:id} : Updates an existing watchList.
     *
     * @param id the id of the watchList to save.
     * @param watchList the watchList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchList,
     * or with status {@code 400 (Bad Request)} if the watchList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the watchList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/watch-lists/{id}")
    public ResponseEntity<WatchList> updateWatchList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchList watchList
    ) throws URISyntaxException {
        log.debug("REST request to update WatchList : {}, {}", id, watchList);
        if (watchList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WatchList result = watchListRepository.save(watchList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, watchList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /watch-lists/:id} : Partial updates given fields of an existing watchList, field will ignore if it is null
     *
     * @param id the id of the watchList to save.
     * @param watchList the watchList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchList,
     * or with status {@code 400 (Bad Request)} if the watchList is not valid,
     * or with status {@code 404 (Not Found)} if the watchList is not found,
     * or with status {@code 500 (Internal Server Error)} if the watchList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/watch-lists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WatchList> partialUpdateWatchList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchList watchList
    ) throws URISyntaxException {
        log.debug("REST request to partial update WatchList partially : {}, {}", id, watchList);
        if (watchList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WatchList> result = watchListRepository
            .findById(watchList.getId())
            .map(
                existingWatchList -> {
                    return existingWatchList;
                }
            )
            .map(watchListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, watchList.getId().toString())
        );
    }

    /**
     * {@code GET  /watch-lists} : get all the watchLists.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watchLists in body.
     */
    @GetMapping("/watch-lists")
    public List<WatchList> getAllWatchLists(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all WatchLists");
        return watchListRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /watch-lists/:id} : get the "id" watchList.
     *
     * @param id the id of the watchList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/watch-lists/{id}")
    public ResponseEntity<WatchList> getWatchList(@PathVariable Long id) {
        log.debug("REST request to get WatchList : {}", id);
        Optional<WatchList> watchList = watchListRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(watchList);
    }

    /**
     * {@code DELETE  /watch-lists/:id} : delete the "id" watchList.
     *
     * @param id the id of the watchList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/watch-lists/{id}")
    public ResponseEntity<Void> deleteWatchList(@PathVariable Long id) {
        log.debug("REST request to delete WatchList : {}", id);
        watchListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
