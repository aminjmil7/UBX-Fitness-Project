package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipementMapperTest {

    private EquipementMapper equipementMapper;

    @BeforeEach
    public void setUp() {
        equipementMapper = new EquipementMapperImpl();
    }
}
