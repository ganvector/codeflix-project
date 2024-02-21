package com.codeflix.admin.catalogo;

import com.codeflix.admin.catalogo.infrastructure.config.WebServerConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test-e2e")
@Inherited
@SpringBootTest(classes = WebServerConfig.class)
@AutoConfigureMockMvc
public @interface E2ETest {
}
