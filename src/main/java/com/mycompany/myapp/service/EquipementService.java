package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EquipementDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Equipement}.
 */
public interface EquipementService {
    /**
     * Save a equipement.
     *
     * @param equipementDTO the entity to save.
     * @return the persisted entity.
     */
    EquipementDTO save(EquipementDTO equipementDTO);

    /**
     * Partially updates a equipement.
     *
     * @param equipementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipementDTO> partialUpdate(EquipementDTO equipementDTO);

    /**
     * Get all the equipements.
     *
     * @return the list of entities.
     */
    List<EquipementDTO> findAll();

    /**
     * Get the "id" equipement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipementDTO> findOne(Long id);

    /**
     * Delete the "id" equipement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
