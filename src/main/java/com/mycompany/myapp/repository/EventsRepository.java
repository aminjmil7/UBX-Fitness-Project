package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Events;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Events entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventsRepository extends JpaRepository<Events, Long>, JpaSpecificationExecutor<Events> {}
