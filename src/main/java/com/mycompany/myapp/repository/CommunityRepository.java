package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Community;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Community entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {}
