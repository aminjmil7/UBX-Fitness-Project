package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipementDTO.class);
        EquipementDTO equipementDTO1 = new EquipementDTO();
        equipementDTO1.setId(1L);
        EquipementDTO equipementDTO2 = new EquipementDTO();
        assertThat(equipementDTO1).isNotEqualTo(equipementDTO2);
        equipementDTO2.setId(equipementDTO1.getId());
        assertThat(equipementDTO1).isEqualTo(equipementDTO2);
        equipementDTO2.setId(2L);
        assertThat(equipementDTO1).isNotEqualTo(equipementDTO2);
        equipementDTO1.setId(null);
        assertThat(equipementDTO1).isNotEqualTo(equipementDTO2);
    }
}
