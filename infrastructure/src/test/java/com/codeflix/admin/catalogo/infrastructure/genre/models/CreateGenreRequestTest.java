package com.codeflix.admin.catalogo.infrastructure.genre.models;

import com.codeflix.admin.catalogo.JacksonTest;
import com.codeflix.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
class CreateGenreRequestTest {

    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var response = new CreateGenreRequest(
                expectedName,
                expectedIsActive,
                expectedCategories
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Action";
        final var expectedCategory = "789";
        final var expectedIsActive = true;

        final var json = """
        {
            "name": "%s",
            "categories_id": ["%s"],
            "is_active": "%s"
        }
        """.formatted(expectedName, expectedCategory, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }


}