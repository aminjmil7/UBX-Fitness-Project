package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.criteria.ReportCriteria;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.mapper.ReportMapper;
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
 * Service for executing complex queries for {@link Report} entities in the database.
 * The main input is a {@link ReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReportDTO} or a {@link Page} of {@link ReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportQueryService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    /**
     * Return a {@link List} of {@link ReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReportDTO> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportMapper.toDto(reportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportDTO> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page).map(reportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Report> createSpecification(ReportCriteria criteria) {
        Specification<Report> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMail(), Report_.mail));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Report_.message));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Report_.date));
            }
            if (criteria.getMediaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMediaId(), root -> root.join(Report_.media, JoinType.LEFT).get(Media_.id))
                    );
            }
            if (criteria.getEquipementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEquipementId(),
                            root -> root.join(Report_.equipement, JoinType.LEFT).get(Equipement_.id)
                        )
                    );
            }
            if (criteria.getParkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParkId(), root -> root.join(Report_.park, JoinType.LEFT).get(Park_.id))
                    );
            }
        }
        return specification;
    }
}
