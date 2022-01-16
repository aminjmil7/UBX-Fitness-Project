package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Media;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Media entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {}
