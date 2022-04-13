package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalkthroughTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Walkthrough.class);
        Walkthrough walkthrough1 = new Walkthrough();
        walkthrough1.setId(1L);
        Walkthrough walkthrough2 = new Walkthrough();
        walkthrough2.setId(walkthrough1.getId());
        assertThat(walkthrough1).isEqualTo(walkthrough2);
        walkthrough2.setId(2L);
        assertThat(walkthrough1).isNotEqualTo(walkthrough2);
        walkthrough1.setId(null);
        assertThat(walkthrough1).isNotEqualTo(walkthrough2);
    }
}
