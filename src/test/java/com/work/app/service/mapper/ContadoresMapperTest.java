package com.work.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContadoresMapperTest {

    private ContadoresMapper contadoresMapper;

    @BeforeEach
    public void setUp() {
        contadoresMapper = new ContadoresMapperImpl();
    }
}
