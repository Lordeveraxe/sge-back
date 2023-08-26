package com.work.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrosMapperTest {

    private RegistrosMapper registrosMapper;

    @BeforeEach
    public void setUp() {
        registrosMapper = new RegistrosMapperImpl();
    }
}
