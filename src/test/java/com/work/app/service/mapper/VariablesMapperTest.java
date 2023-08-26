package com.work.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VariablesMapperTest {

    private VariablesMapper variablesMapper;

    @BeforeEach
    public void setUp() {
        variablesMapper = new VariablesMapperImpl();
    }
}
