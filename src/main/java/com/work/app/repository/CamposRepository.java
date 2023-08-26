package com.work.app.repository;

import com.work.app.domain.Campos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Campos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CamposRepository extends JpaRepository<Campos, Long> {}
