package com.ticker.web.rest;

import com.ticker.domain.TickerProfile;
import com.ticker.repository.TickerProfileRepository;
import com.ticker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.ticker.domain.TickerProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TickerProfileResource {

    private final Logger log = LoggerFactory.getLogger(TickerProfileResource.class);

    private static final String ENTITY_NAME = "tickerProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TickerProfileRepository tickerProfileRepository;

    public TickerProfileResource(TickerProfileRepository tickerProfileRepository) {
        this.tickerProfileRepository = tickerProfileRepository;
    }

    /**
     * {@code POST  /ticker-profiles} : Create a new tickerProfile.
     *
     * @param tickerProfile the tickerProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickerProfile, or with status {@code 400 (Bad Request)} if the tickerProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticker-profiles")
    public ResponseEntity<TickerProfile> createTickerProfile(@Valid @RequestBody TickerProfile tickerProfile) throws URISyntaxException {
        log.debug("REST request to save TickerProfile : {}", tickerProfile);
        if (tickerProfile.getId() != null) {
            throw new BadRequestAlertException("A new tickerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TickerProfile result = tickerProfileRepository.save(tickerProfile);
        return ResponseEntity
            .created(new URI("/api/ticker-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticker-profiles/:id} : Updates an existing tickerProfile.
     *
     * @param id the id of the tickerProfile to save.
     * @param tickerProfile the tickerProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerProfile,
     * or with status {@code 400 (Bad Request)} if the tickerProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickerProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticker-profiles/{id}")
    public ResponseEntity<TickerProfile> updateTickerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TickerProfile tickerProfile
    ) throws URISyntaxException {
        log.debug("REST request to update TickerProfile : {}, {}", id, tickerProfile);
        if (tickerProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TickerProfile result = tickerProfileRepository.save(tickerProfile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tickerProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ticker-profiles/:id} : Partial updates given fields of an existing tickerProfile, field will ignore if it is null
     *
     * @param id the id of the tickerProfile to save.
     * @param tickerProfile the tickerProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickerProfile,
     * or with status {@code 400 (Bad Request)} if the tickerProfile is not valid,
     * or with status {@code 404 (Not Found)} if the tickerProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the tickerProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ticker-profiles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TickerProfile> partialUpdateTickerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TickerProfile tickerProfile
    ) throws URISyntaxException {
        log.debug("REST request to partial update TickerProfile partially : {}, {}", id, tickerProfile);
        if (tickerProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickerProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tickerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TickerProfile> result = tickerProfileRepository
            .findById(tickerProfile.getId())
            .map(
                existingTickerProfile -> {
                    if (tickerProfile.getTickerSymbol() != null) {
                        existingTickerProfile.setTickerSymbol(tickerProfile.getTickerSymbol());
                    }
                    if (tickerProfile.getIndustry() != null) {
                        existingTickerProfile.setIndustry(tickerProfile.getIndustry());
                    }
                    if (tickerProfile.getName() != null) {
                        existingTickerProfile.setName(tickerProfile.getName());
                    }
                    if (tickerProfile.getPhone() != null) {
                        existingTickerProfile.setPhone(tickerProfile.getPhone());
                    }
                    if (tickerProfile.getWebsite() != null) {
                        existingTickerProfile.setWebsite(tickerProfile.getWebsite());
                    }
                    if (tickerProfile.getDescription() != null) {
                        existingTickerProfile.setDescription(tickerProfile.getDescription());
                    }
                    if (tickerProfile.getFullTimeEmployees() != null) {
                        existingTickerProfile.setFullTimeEmployees(tickerProfile.getFullTimeEmployees());
                    }

                    return existingTickerProfile;
                }
            )
            .map(tickerProfileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tickerProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /ticker-profiles} : get all the tickerProfiles.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickerProfiles in body.
     */
    @GetMapping("/ticker-profiles")
    public List<TickerProfile> getAllTickerProfiles(@RequestParam(required = false) String filter) {
        if ("ticker-is-null".equals(filter)) {
            log.debug("REST request to get all TickerProfiles where ticker is null");
            return StreamSupport
                .stream(tickerProfileRepository.findAll().spliterator(), false)
                .filter(tickerProfile -> tickerProfile.getTicker() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all TickerProfiles");
        return tickerProfileRepository.findAll();
    }

    /**
     * {@code GET  /ticker-profiles/:id} : get the "id" tickerProfile.
     *
     * @param id the id of the tickerProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickerProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticker-profiles/{id}")
    public ResponseEntity<TickerProfile> getTickerProfile(@PathVariable Long id) {
        log.debug("REST request to get TickerProfile : {}", id);
        Optional<TickerProfile> tickerProfile = tickerProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tickerProfile);
    }

    /**
     * {@code DELETE  /ticker-profiles/:id} : delete the "id" tickerProfile.
     *
     * @param id the id of the tickerProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticker-profiles/{id}")
    public ResponseEntity<Void> deleteTickerProfile(@PathVariable Long id) {
        log.debug("REST request to delete TickerProfile : {}", id);
        tickerProfileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
