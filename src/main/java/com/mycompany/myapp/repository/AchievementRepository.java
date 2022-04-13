package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Achievement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Achievement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {}
