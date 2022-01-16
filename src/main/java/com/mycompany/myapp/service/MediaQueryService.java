package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Media;
import com.mycompany.myapp.repository.MediaRepository;
import com.mycompany.myapp.service.criteria.MediaCriteria;
import com.mycompany.myapp.service.dto.MediaDTO;
import com.mycompany.myapp.service.mapper.MediaMapper;
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
 * Service for executing complex queries for {@link Media} entities in the database.
 * The main input is a {@link MediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MediaDTO} or a {@link Page} of {@link MediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MediaQueryService extends QueryService<Media> {

    private final Logger log = LoggerFactory.getLogger(MediaQueryService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaQueryService(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Return a {@link List} of {@link MediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MediaDTO> findByCriteria(MediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaMapper.toDto(mediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MediaDTO> findByCriteria(MediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.findAll(specification, page).map(mediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.count(specification);
    }

    /**
     * Function to convert {@link MediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Media> createSpecification(MediaCriteria criteria) {
        Specification<Media> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Media_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), Media_.fileName));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), Media_.filePath));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileType(), Media_.fileType));
            }
            if (criteria.getAuthType() != null) {
                specification = specification.and(buildSpecification(criteria.getAuthType(), Media_.authType));
            }
            if (criteria.getParkId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParkId(), root -> root.join(Media_.park, JoinType.LEFT).get(Park_.id))
                    );
            }
            if (criteria.getEquipementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEquipementId(),
                            root -> root.join(Media_.equipement, JoinType.LEFT).get(Equipement_.id)
                        )
                    );
            }
            if (criteria.getReportId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReportId(), root -> root.join(Media_.report, JoinType.LEFT).get(Report_.id))
                    );
            }
        }
        return specification;
    }
}
