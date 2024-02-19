package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.ControllerTest;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void shouldCreateACategoryWhenAllInputsAreValid() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.create("123")));

        final var request =  MockMvcRequestBuilders.post("/categories")
               .contentType(MediaType.APPLICATION_JSON)
               .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location", "/categories/123")
                );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                    && Objects.equals(expectedDescription, cmd.description())
                    && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void shouldReturnAnExceptionWhenInputNameIsNotValid() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error("'name' should not be null"))));

        final var request =  MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void shouldReturnADomainExceptionWhenInputNameIsNotValid() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.raise(new Error("'name' should not be null")));

        final var request =  MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
}