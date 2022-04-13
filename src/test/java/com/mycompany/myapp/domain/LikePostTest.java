package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikePostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikePost.class);
        LikePost likePost1 = new LikePost();
        likePost1.setId(1L);
        LikePost likePost2 = new LikePost();
        likePost2.setId(likePost1.getId());
        assertThat(likePost1).isEqualTo(likePost2);
        likePost2.setId(2L);
        assertThat(likePost1).isNotEqualTo(likePost2);
        likePost1.setId(null);
        assertThat(likePost1).isNotEqualTo(likePost2);
    }
}
