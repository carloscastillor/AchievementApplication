package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PersonalizedAchievement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonalizedAchievement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalizedAchievementRepository extends JpaRepository<PersonalizedAchievement, Long> {}
