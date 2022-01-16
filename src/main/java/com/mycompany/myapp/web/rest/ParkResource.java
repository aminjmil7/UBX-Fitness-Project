package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.repository.ParkRepository;
import com.mycompany.myapp.service.ParkQueryService;
import com.mycompany.myapp.service.ParkService;
import com.mycompany.myapp.service.criteria.ParkCriteria;
import com.mycompany.myapp.service.dto.ParkDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Park}.
 */
@RestController
@RequestMapping("/api")
public class ParkResource {

    private final Logger log = LoggerFactory.getLogger(ParkResource.class);

    private static final String ENTITY_NAME = "park";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParkService parkService;

    private final ParkRepository parkRepository;

    private final ParkQueryService parkQueryService;

    public ParkResource(ParkService parkService, ParkRepository parkRepository, ParkQueryService parkQueryService) {
        this.parkService = parkService;
        this.parkRepository = parkRepository;
        this.parkQueryService = parkQueryService;
    }

    /**
     * {@code POST  /parks} : Create a new park.
     *
     * @param parkDTO the parkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parkDTO, or with status {@code 400 (Bad Request)} if the park has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parks")
    public ResponseEntity<ParkDTO> createPark(@RequestBody ParkDTO parkDTO) throws URISyntaxException {
        log.debug("REST request to save Park : {}", parkDTO);
        if (parkDTO.getId() != null) {
            throw new BadRequestAlertException("A new park cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParkDTO result = parkService.save(parkDTO);
        return ResponseEntity
            .created(new URI("/api/parks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parks/:id} : Updates an existing park.
     *
     * @param id the id of the parkDTO to save.
     * @param parkDTO the parkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkDTO,
     * or with status {@code 400 (Bad Request)} if the parkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parks/{id}")
    public ResponseEntity<ParkDTO> updatePark(@PathVariable(value = "id", required = false) final Long id, @RequestBody ParkDTO parkDTO)
        throws URISyntaxException {
        log.debug("REST request to update Park : {}, {}", id, parkDTO);
        if (parkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParkDTO result = parkService.save(parkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parks/:id} : Partial updates given fields of an existing park, field will ignore if it is null
     *
     * @param id the id of the parkDTO to save.
     * @param parkDTO the parkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkDTO,
     * or with status {@code 400 (Bad Request)} if the parkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParkDTO> partialUpdatePark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParkDTO parkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Park partially : {}, {}", id, parkDTO);
        if (parkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParkDTO> result = parkService.partialUpdate(parkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parks} : get all the parks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parks in body.
     */
    @GetMapping("/parks")
    public ResponseEntity<List<ParkDTO>> getAllParks(ParkCriteria criteria) {
        log.debug("REST request to get Parks by criteria: {}", criteria);
        List<ParkDTO> entityList = parkQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /parks/count} : count all the parks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parks/count")
    public ResponseEntity<Long> countParks(ParkCriteria criteria) {
        log.debug("REST request to count Parks by criteria: {}", criteria);
        return ResponseEntity.ok().body(parkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parks/:id} : get the "id" park.
     *
     * @param id the id of the parkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parks/{id}")
    public ResponseEntity<ParkDTO> getPark(@PathVariable Long id) {
        log.debug("REST request to get Park : {}", id);
        Optional<ParkDTO> parkDTO = parkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parkDTO);
    }

    /**
     * {@code DELETE  /parks/:id} : delete the "id" park.
     *
     * @param id the id of the parkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parks/{id}")
    public ResponseEntity<Void> deletePark(@PathVariable Long id) {
        log.debug("REST request to delete Park : {}", id);
        parkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /parks} : get all the parks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parks in body.
     */
    @GetMapping("/parksByDistance/{distanceMax},{lat},{lon}")
    public List<Park> getParksByDistance(@PathVariable double distanceMax, @PathVariable BigDecimal lat, @PathVariable BigDecimal lon) {
        List<Park> parksList = parkQueryService.getParksByDistance();
        List<Park> parksinInterval = new ArrayList<Park>();
        if (parksList.size() != 0) {
            for (Park park : parksList) {
                final int R = 6371; // Radius of the earth

                double latDistance = Math.toRadians(park.getLatitude().doubleValue() - lat.doubleValue());
                double lonDistance = Math.toRadians(park.getLongtitude().doubleValue() - lon.doubleValue());

                double a =
                    Math.sin(latDistance / 2) *
                    Math.sin(latDistance / 2) +
                    Math.cos(Math.toRadians(lat.doubleValue())) *
                    Math.cos(Math.toRadians(lat.doubleValue())) *
                    Math.sin(lonDistance / 2) *
                    Math.sin(lonDistance / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distance = Math.pow((R * c), 2);

                if (distance <= distanceMax && park.getVerified()) {
                    parksinInterval.add(park);
                }
            }
        }

        return parksinInterval;
    }
}
