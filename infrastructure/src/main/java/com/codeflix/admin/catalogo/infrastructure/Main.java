package com.codeflix.admin.catalogo.infrastructure;


import com.codeflix.admin.catalogo.infrastructure.config.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }
}
