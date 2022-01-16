package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Equipement;
import com.mycompany.myapp.repository.EquipementRepository;
import com.mycompany.myapp.service.criteria.EquipementCriteria;
import com.mycompany.myapp.service.dto.EquipementDTO;
import com.mycompany.myapp.service.mapper.EquipementMapper;
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
 * Service for executing complex queries for {@link Equipement} entities in the database.
 * The main input is a {@link EquipementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EquipementDTO} or a {@link Page} of {@link EquipementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipementQueryService extends QueryService<Equipement> {

    private final Logger log = LoggerFactory.getLogger(EquipementQueryService.class);

    private final EquipementRepository equipementRepository;

    private final EquipementMapper equipementMapper;

    public EquipementQueryService(EquipementRepository equipementRepository, EquipementMapper equipementMapper) {
        this.equipementRepository = equipementRepository;
        this.equipementMapper = equipementMapper;
    }

    /**
     * Return a {@link List} of {@link EquipementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EquipementDTO> findByCriteria(EquipementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Equipement> specification = createSpecification(criteria);
        return equipementMapper.toDto(equipementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EquipementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipementDTO> findByCriteria(EquipementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Equipement> specification = createSpecification(criteria);
        return equipementRepository.findAll(specification, page).map(equipementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Equipement> specification = createSpecification(criteria);
        return equipementRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Equipement> createSpecification(EquipementCriteria criteria) {
        Specification<Equipement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Equipement_.id));
            }
            if (criteria.getModelName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelName(), Equipement_.modelName));
            }
            if (criteria.getModelNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelNumber(), Equipement_.modelNumber));
            }
            if (criteria.getInstruction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInstruction(), Equipement_.instruction));
            }
            if (criteria.getVerified() != null) {
                specification = specification.and(buildSpecification(criteria.getVerified(), Equipement_.verified));
            }
            if (criteria.getReportId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReportId(), root -> root.join(Equipement_.reports, JoinType.LEFT).get(Report_.id))
                    );
            }
            if (criteria.getMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMediaId(), root -> root.join(Equipement_.media, JoinType.LEFT).get(Media_.id))
                    );
            }
            if (criteria.getParkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParkId(), root -> root.join(Equipement_.park, JoinType.LEFT).get(Park_.id))
                    );
            }
        }
        return specification;
    }
}
