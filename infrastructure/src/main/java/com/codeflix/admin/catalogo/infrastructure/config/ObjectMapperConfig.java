package com.codeflix.admin.catalogo.infrastructure.config;

import com.codeflix.admin.catalogo.infrastructure.config.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.getMapper();
    }

}