package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Equipement;
import com.mycompany.myapp.repository.EquipementRepository;
import com.mycompany.myapp.service.EquipementService;
import com.mycompany.myapp.service.dto.EquipementDTO;
import com.mycompany.myapp.service.mapper.EquipementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Equipement}.
 */
@Service
@Transactional
public class EquipementServiceImpl implements EquipementService {

    private final Logger log = LoggerFactory.getLogger(EquipementServiceImpl.class);

    private final EquipementRepository equipementRepository;

    private final EquipementMapper equipementMapper;

    public EquipementServiceImpl(EquipementRepository equipementRepository, EquipementMapper equipementMapper) {
        this.equipementRepository = equipementRepository;
        this.equipementMapper = equipementMapper;
    }

    @Override
    public EquipementDTO save(EquipementDTO equipementDTO) {
        log.debug("Request to save Equipement : {}", equipementDTO);
        Equipement equipement = equipementMapper.toEntity(equipementDTO);
        equipement = equipementRepository.save(equipement);
        return equipementMapper.toDto(equipement);
    }

    @Override
    public Optional<EquipementDTO> partialUpdate(EquipementDTO equipementDTO) {
        log.debug("Request to partially update Equipement : {}", equipementDTO);

        return equipementRepository
            .findById(equipementDTO.getId())
            .map(
                existingEquipement -> {
                    equipementMapper.partialUpdate(existingEquipement, equipementDTO);
                    return existingEquipement;
                }
            )
            .map(equipementRepository::save)
            .map(equipementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipementDTO> findAll() {
        log.debug("Request to get all Equipements");
        return equipementRepository.findAll().stream().map(equipementMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipementDTO> findOne(Long id) {
        log.debug("Request to get Equipement : {}", id);
        return equipementRepository.findById(id).map(equipementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equipement : {}", id);
        equipementRepository.deleteById(id);
    }
}
