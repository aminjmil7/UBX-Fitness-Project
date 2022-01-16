package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EventsRepository;
import com.mycompany.myapp.service.EventsQueryService;
import com.mycompany.myapp.service.EventsService;
import com.mycompany.myapp.service.criteria.EventsCriteria;
import com.mycompany.myapp.service.dto.EventsDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Events}.
 */
@RestController
@RequestMapping("/api")
public class EventsResource {

    private final Logger log = LoggerFactory.getLogger(EventsResource.class);

    private static final String ENTITY_NAME = "events";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventsService eventsService;

    private final EventsRepository eventsRepository;

    private final EventsQueryService eventsQueryService;

    public EventsResource(EventsService eventsService, EventsRepository eventsRepository, EventsQueryService eventsQueryService) {
        this.eventsService = eventsService;
        this.eventsRepository = eventsRepository;
        this.eventsQueryService = eventsQueryService;
    }

    /**
     * {@code POST  /events} : Create a new events.
     *
     * @param eventsDTO the eventsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventsDTO, or with status {@code 400 (Bad Request)} if the events has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public ResponseEntity<EventsDTO> createEvents(@RequestBody EventsDTO eventsDTO) throws URISyntaxException {
        log.debug("REST request to save Events : {}", eventsDTO);
        if (eventsDTO.getId() != null) {
            throw new BadRequestAlertException("A new events cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventsDTO result = eventsService.save(eventsDTO);
        return ResponseEntity
            .created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /events/:id} : Updates an existing events.
     *
     * @param id the id of the eventsDTO to save.
     * @param eventsDTO the eventsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventsDTO,
     * or with status {@code 400 (Bad Request)} if the eventsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events/{id}")
    public ResponseEntity<EventsDTO> updateEvents(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventsDTO eventsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Events : {}, {}", id, eventsDTO);
        if (eventsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventsDTO result = eventsService.save(eventsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /events/:id} : Partial updates given fields of an existing events, field will ignore if it is null
     *
     * @param id the id of the eventsDTO to save.
     * @param eventsDTO the eventsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventsDTO,
     * or with status {@code 400 (Bad Request)} if the eventsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/events/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EventsDTO> partialUpdateEvents(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventsDTO eventsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Events partially : {}, {}", id, eventsDTO);
        if (eventsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventsDTO> result = eventsService.partialUpdate(eventsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /events} : get all the events.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventsDTO>> getAllEvents(EventsCriteria criteria) {
        log.debug("REST request to get Events by criteria: {}", criteria);
        List<EventsDTO> entityList = eventsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /events/count} : count all the events.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/events/count")
    public ResponseEntity<Long> countEvents(EventsCriteria criteria) {
        log.debug("REST request to count Events by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /events/:id} : get the "id" events.
     *
     * @param id the id of the eventsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<EventsDTO> getEvents(@PathVariable Long id) {
        log.debug("REST request to get Events : {}", id);
        Optional<EventsDTO> eventsDTO = eventsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventsDTO);
    }

    /**
     * {@code DELETE  /events/:id} : delete the "id" events.
     *
     * @param id the id of the eventsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvents(@PathVariable Long id) {
        log.debug("REST request to delete Events : {}", id);
        eventsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
