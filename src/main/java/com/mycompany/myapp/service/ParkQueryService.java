package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.repository.ParkRepository;
import com.mycompany.myapp.service.criteria.ParkCriteria;
import com.mycompany.myapp.service.dto.ParkDTO;
import com.mycompany.myapp.service.mapper.ParkMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Park} entities in the database.
 * The main input is a {@link ParkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParkDTO} or a {@link Page} of {@link ParkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParkQueryService extends QueryService<Park> {

    private final Logger log = LoggerFactory.getLogger(ParkQueryService.class);

    private final ParkRepository parkRepository;

    private final ParkMapper parkMapper;

    public ParkQueryService(ParkRepository parkRepository, ParkMapper parkMapper) {
        this.parkRepository = parkRepository;
        this.parkMapper = parkMapper;
    }

    /**
     * Return a {@link List} of {@link ParkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParkDTO> findByCriteria(ParkCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Park> specification = createSpecification(criteria);
        return parkMapper.toDto(parkRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParkDTO> findByCriteria(ParkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Park> specification = createSpecification(criteria);
        return parkRepository.findAll(specification, page).map(parkMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParkCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Park> specification = createSpecification(criteria);
        return parkRepository.count(specification);
    }

    /**
     * Function to convert {@link ParkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Park> createSpecification(ParkCriteria criteria) {
        Specification<Park> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Park_.id));
            }
            if (criteria.getParkName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParkName(), Park_.parkName));
            }
            if (criteria.getParkAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParkAddress(), Park_.parkAddress));
            }
            if (criteria.getLongtitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongtitude(), Park_.longtitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Park_.latitude));
            }
            if (criteria.getVerified() != null) {
                specification = specification.and(buildSpecification(criteria.getVerified(), Park_.verified));
            }
            if (criteria.getDateInstall() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateInstall(), Park_.dateInstall));
            }
            if (criteria.getDateOpen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOpen(), Park_.dateOpen));
            }
            if (criteria.getDateClose() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateClose(), Park_.dateClose));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Park_.note));
            }
            if (criteria.getReseller() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReseller(), Park_.reseller));
            }
            if (criteria.getEquipementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEquipementId(),
                            root -> root.join(Park_.equipements, JoinType.LEFT).get(Equipement_.id)
                        )
                    );
            }
            if (criteria.getMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMediaId(), root -> root.join(Park_.media, JoinType.LEFT).get(Media_.id))
                    );
            }
            if (criteria.getReportId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReportId(), root -> root.join(Park_.reports, JoinType.LEFT).get(Report_.id))
                    );
            }
        }
        return specification;
    }
}
