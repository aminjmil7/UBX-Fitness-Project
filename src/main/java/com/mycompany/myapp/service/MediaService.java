package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MediaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Media}.
 */
public interface MediaService {
    /**
     * Save a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    MediaDTO save(MediaDTO mediaDTO);

    /**
     * Partially updates a media.
     *
     * @param mediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MediaDTO> partialUpdate(MediaDTO mediaDTO);

    /**
     * Get all the media.
     *
     * @return the list of entities.
     */
    List<MediaDTO> findAll();

    /**
     * Get the "id" media.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MediaDTO> findOne(Long id);

    /**
     * Delete the "id" media.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
