package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Park;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Park entity.
 */
@SuppressWarnings("")
@Repository
public interface ParkRepository extends JpaRepository<Park, Long>, JpaSpecificationExecutor<Park> {}
