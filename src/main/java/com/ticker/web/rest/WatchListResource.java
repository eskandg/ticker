package com.ticker.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticker.domain.User;
import com.ticker.domain.WatchList;
import com.ticker.repository.WatchListRepository;
import com.ticker.service.UserService;
import com.ticker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
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

    private final UserService userService;
    private final WatchListRepository watchListRepository;

    public WatchListResource(UserService userService, WatchListRepository watchListRepository) {
        this.userService = userService;
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
    public ResponseEntity<Map> createWatchList(@Valid @RequestBody WatchList watchList) throws URISyntaxException {
        log.debug("REST request to save WatchList : {}", watchList);
        if (watchList.getId() != null) {
            throw new BadRequestAlertException("A new watchList cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Map response = new HashMap();
        response.put("response", "Created watchlist element");

        WatchList result = watchListRepository.save(watchList);
        return ResponseEntity
            .created(new URI("/api/watch-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(response);
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
        @Valid @RequestBody WatchList watchList
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
        @NotNull @RequestBody WatchList watchList
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
                    if (watchList.getTickerSymbol() != null) {
                        existingWatchList.setTickerSymbol(watchList.getTickerSymbol());
                    }

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
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watchLists in body.
     */
    @GetMapping("/watch-lists")
    public List<WatchList> getAllWatchLists() {
        log.debug("REST request to get all WatchLists");
        return watchListRepository.findAll();
    }

    /**
     * {@code GET  /watch-lists/:username} : get the tickers associated with the users watchList.
     *
     * @param username the user of the watchList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/watch-lists/{username}")
    public ResponseEntity<List<Map>> getWatchList(@PathVariable String username) throws JsonProcessingException {
        log.debug("REST request to get WatchList : {}", username);
        ObjectMapper objectMapper = new ObjectMapper();

        Optional<User> userOptional = userService.getPublicUser(username);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("Invalid user", ENTITY_NAME, "userinvalid");
        }

        List<WatchList> watchList = watchListRepository.findAllByUser(userOptional.get()).get();

        List<Map> map = new ArrayList<Map>();

        // Do not send all of the users details with the response since it is public
        for (WatchList watchListElement : watchList) {
            Map response = objectMapper.readValue(objectMapper.writeValueAsString(watchListElement), Map.class);
            Map userResponse = (Map) response.get("user");
            userResponse.remove("firstName");
            userResponse.remove("lastName");
            userResponse.remove("email");
            userResponse.remove("activated");
            userResponse.remove("langKey");
            userResponse.remove("imageUrl");
            userResponse.remove("resetDate");
            map.add(response);
        }
        return ResponseEntity.ok(map);
    }

    /**
     * {@code GET  /watch-lists/:username?tickerSymbol} : get the ticker associated with the users watchList.
     *
     * @param username, tickerSymbol the ticker associated with the user of the watchList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/watch-lists/{username}/{tickerSymbol}")
    public ResponseEntity<Map> getWatchListElement(@PathVariable String username, @PathVariable String tickerSymbol)
        throws JsonProcessingException {
        log.debug("REST request to get WatchList : {}", tickerSymbol);
        ObjectMapper objectMapper = new ObjectMapper();

        Optional<User> userOptional = userService.getPublicUser(username);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("Invalid user", ENTITY_NAME, "userinvalid");
        }

        Optional<WatchList> watchList = watchListRepository.findFirstByUserAndTickerSymbol(userOptional.get(), tickerSymbol);

        // Do not send all of the users details with the response since it is public
        Map response = objectMapper.readValue(objectMapper.writeValueAsString(watchList.get()), Map.class);
        Map userResponse = (Map) response.get("user");
        userResponse.remove("firstName");
        userResponse.remove("lastName");
        userResponse.remove("email");
        userResponse.remove("activated");
        userResponse.remove("langKey");
        userResponse.remove("imageUrl");
        userResponse.remove("resetDate");

        return ResponseEntity.ok(response);
    }

    /**
     * {@code DELETE  /watch-lists/:username?tickerSymbol} : delete the "username?tickerSymbol" watchList.
     *
     * @param username, tickerSymbol : the ticker associated with the user of the watchList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/watch-lists/{username}/{tickerSymbol}")
    public ResponseEntity<Void> deleteWatchListElement(@PathVariable String username, @PathVariable String tickerSymbol) {
        log.debug("REST request to delete WatchList by current user and tickerSymbol : {}", tickerSymbol);

        Optional<User> userOptional = userService.getPublicUser(username);
        if (!userOptional.isPresent()) {
            throw new BadRequestAlertException("Invalid user", ENTITY_NAME, "userinvalid");
        }

        watchListRepository.deleteAllByUserAndTickerSymbol(userOptional.get(), tickerSymbol);

        List<String> response = new ArrayList<String>();
        response.add(tickerSymbol);
        response.add(username);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, response.toString()))
            .build();
    }
}
