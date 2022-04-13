package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalizedAchievementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalizedAchievement.class);
        PersonalizedAchievement personalizedAchievement1 = new PersonalizedAchievement();
        personalizedAchievement1.setId(1L);
        PersonalizedAchievement personalizedAchievement2 = new PersonalizedAchievement();
        personalizedAchievement2.setId(personalizedAchievement1.getId());
        assertThat(personalizedAchievement1).isEqualTo(personalizedAchievement2);
        personalizedAchievement2.setId(2L);
        assertThat(personalizedAchievement1).isNotEqualTo(personalizedAchievement2);
        personalizedAchievement1.setId(null);
        assertThat(personalizedAchievement1).isNotEqualTo(personalizedAchievement2);
    }
}
