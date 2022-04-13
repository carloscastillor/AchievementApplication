package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeMessage.class);
        LikeMessage likeMessage1 = new LikeMessage();
        likeMessage1.setId(1L);
        LikeMessage likeMessage2 = new LikeMessage();
        likeMessage2.setId(likeMessage1.getId());
        assertThat(likeMessage1).isEqualTo(likeMessage2);
        likeMessage2.setId(2L);
        assertThat(likeMessage1).isNotEqualTo(likeMessage2);
        likeMessage1.setId(null);
        assertThat(likeMessage1).isNotEqualTo(likeMessage2);
    }
}
