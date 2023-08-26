package com.work.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CamposMapperTest {

    private CamposMapper camposMapper;

    @BeforeEach
    public void setUp() {
        camposMapper = new CamposMapperImpl();
    }
}
