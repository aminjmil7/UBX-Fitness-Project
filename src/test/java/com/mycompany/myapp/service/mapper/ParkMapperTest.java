package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkMapperTest {

    private ParkMapper parkMapper;

    @BeforeEach
    public void setUp() {
        parkMapper = new ParkMapperImpl();
    }
}
