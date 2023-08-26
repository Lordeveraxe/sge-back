package com.work.app.repository;

import com.work.app.domain.Contadores;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contadores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContadoresRepository extends JpaRepository<Contadores, Long> {}
