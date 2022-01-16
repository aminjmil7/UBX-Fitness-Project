package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquipementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipement.class);
        Equipement equipement1 = new Equipement();
        equipement1.setId(1L);
        Equipement equipement2 = new Equipement();
        equipement2.setId(equipement1.getId());
        assertThat(equipement1).isEqualTo(equipement2);
        equipement2.setId(2L);
        assertThat(equipement1).isNotEqualTo(equipement2);
        equipement1.setId(null);
        assertThat(equipement1).isNotEqualTo(equipement2);
    }
}
