package com.work.app.repository;

import com.work.app.domain.Variables;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Variables entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariablesRepository extends JpaRepository<Variables, Long> {}
