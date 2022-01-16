package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EventsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Events}.
 */
public interface EventsService {
    /**
     * Save a events.
     *
     * @param eventsDTO the entity to save.
     * @return the persisted entity.
     */
    EventsDTO save(EventsDTO eventsDTO);

    /**
     * Partially updates a events.
     *
     * @param eventsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventsDTO> partialUpdate(EventsDTO eventsDTO);

    /**
     * Get all the events.
     *
     * @return the list of entities.
     */
    List<EventsDTO> findAll();

    /**
     * Get the "id" events.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventsDTO> findOne(Long id);

    /**
     * Delete the "id" events.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
