package com.work.app.repository;

import com.work.app.domain.Registros;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Registros entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistrosRepository extends JpaRepository<Registros, Long> {}
