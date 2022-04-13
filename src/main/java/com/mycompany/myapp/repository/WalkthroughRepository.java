package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Walkthrough;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Walkthrough entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalkthroughRepository extends JpaRepository<Walkthrough, Long> {}
