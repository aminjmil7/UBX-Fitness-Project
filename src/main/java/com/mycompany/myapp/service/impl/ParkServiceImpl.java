package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.repository.ParkRepository;
import com.mycompany.myapp.service.ParkService;
import com.mycompany.myapp.service.dto.ParkDTO;
import com.mycompany.myapp.service.mapper.ParkMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Park}.
 */
@Service
@Transactional
public class ParkServiceImpl implements ParkService {

    private final Logger log = LoggerFactory.getLogger(ParkServiceImpl.class);

    private final ParkRepository parkRepository;

    private final ParkMapper parkMapper;

    public ParkServiceImpl(ParkRepository parkRepository, ParkMapper parkMapper) {
        this.parkRepository = parkRepository;
        this.parkMapper = parkMapper;
    }

    @Override
    public ParkDTO save(ParkDTO parkDTO) {
        log.debug("Request to save Park : {}", parkDTO);
        Park park = parkMapper.toEntity(parkDTO);
        park = parkRepository.save(park);
        return parkMapper.toDto(park);
    }

    @Override
    public Optional<ParkDTO> partialUpdate(ParkDTO parkDTO) {
        log.debug("Request to partially update Park : {}", parkDTO);

        return parkRepository
            .findById(parkDTO.getId())
            .map(
                existingPark -> {
                    parkMapper.partialUpdate(existingPark, parkDTO);
                    return existingPark;
                }
            )
            .map(parkRepository::save)
            .map(parkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkDTO> findAll() {
        log.debug("Request to get all Parks");
        return parkRepository.findAll().stream().map(parkMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParkDTO> findOne(Long id) {
        log.debug("Request to get Park : {}", id);
        return parkRepository.findById(id).map(parkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Park : {}", id);
        parkRepository.deleteById(id);
    }
}
