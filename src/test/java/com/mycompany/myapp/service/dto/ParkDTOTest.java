package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkDTO.class);
        ParkDTO parkDTO1 = new ParkDTO();
        parkDTO1.setId(1L);
        ParkDTO parkDTO2 = new ParkDTO();
        assertThat(parkDTO1).isNotEqualTo(parkDTO2);
        parkDTO2.setId(parkDTO1.getId());
        assertThat(parkDTO1).isEqualTo(parkDTO2);
        parkDTO2.setId(2L);
        assertThat(parkDTO1).isNotEqualTo(parkDTO2);
        parkDTO1.setId(null);
        assertThat(parkDTO1).isNotEqualTo(parkDTO2);
    }
}
