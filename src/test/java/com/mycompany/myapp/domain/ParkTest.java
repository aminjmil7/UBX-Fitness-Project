package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Park.class);
        Park park1 = new Park();
        park1.setId(1L);
        Park park2 = new Park();
        park2.setId(park1.getId());
        assertThat(park1).isEqualTo(park2);
        park2.setId(2L);
        assertThat(park1).isNotEqualTo(park2);
        park1.setId(null);
        assertThat(park1).isNotEqualTo(park2);
    }
}
