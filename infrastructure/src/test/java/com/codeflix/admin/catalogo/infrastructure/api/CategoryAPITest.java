package com.codeflix.admin.catalogo.infrastructure.api;

import com.codeflix.admin.catalogo.ControllerTest;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.codeflix.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.get.GetCategoryByIdOutput;
import com.codeflix.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.admin.catalogo.application.category.retrieve.list.ListCategoriesOutput;
import com.codeflix.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.codeflix.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void shouldCreateACategoryWhenAllInputsAreValid() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.create("123")));

        final var request =  MockMvcRequestBuilders.post("/categories")
               .contentType(MediaType.APPLICATION_JSON)
               .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"));

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

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error("'name' should not be null"))));

        final var request =  MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

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

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.raise(new Error("'name' should not be null")));

        final var request =  MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void shouldGetACategoryWhenInputIdIsValid() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;
        final var aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        Mockito
                .when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(GetCategoryByIdOutput.create(aCategory));

        final var request =  MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        Mockito
                .verify(getCategoryByIdUseCase, Mockito.times(1))
                .execute(Mockito.eq(expectedId));
    }

    @Test
    public void shouldReturnANotFoundErrorWhenCategoryIsNotFound() throws Exception {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.load("123");

        Mockito
                .when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.raise(Category.class, expectedId));

        final var request =  MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void shouldUpdateACategoryWhenAllInputsAreValid() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;
        final var expectedId = "valid_id";

        final var anInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(UpdateCategoryOutput.create(expectedId)));

        final var request =  MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito
                .verify(updateCategoryUseCase, Mockito.times(1))
                .execute(Mockito.argThat(cmd ->
                    Objects.equals(expectedName, cmd.name())
                            && Objects.equals(expectedDescription, cmd.description())
                            && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void shouldReturnAnExceptionWhenIdNotFound() throws Exception {
        final var expectedId = "not_found";
        final String expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID not_found was not found";

        final var anInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.raise(Category.class, CategoryID.load(expectedId)));

        final var request =  MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito
                .verify(updateCategoryUseCase, Mockito.times(1))
                .execute(Mockito.argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void shouldReturnAnExceptionWhenUpdatingWithInputNameNotValid() throws Exception {
        final var expectedId = "123";
        final String expectedName = "Movies";
        final var expectedDescription = "The universe's best movies";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var anInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito
                .when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        final var request =  MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito
                .verify(updateCategoryUseCase, Mockito.times(1))
                .execute(Mockito.argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void shouldDeleteACategoryWhenInputIdIsValid() throws Exception {
        final var expectedId = "123";

        Mockito.doNothing()
                .when(deleteCategoryUseCase).execute(Mockito.any());

        final var request =  MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito
                .verify(deleteCategoryUseCase, Mockito.times(1))
                .execute(Mockito.eq(expectedId));
    }

    @Test
    public void shouldReturnCategoriesWhenGivenValidParameters() throws Exception {
        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListCategoriesOutput.create(aCategory));

        Mockito.when(listCategoriesUseCase.execute(Mockito.any())).thenReturn(new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedItems
        ));

        final var request =  MockMvcRequestBuilders.get("/categories/")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(query ->
            Objects.equals(expectedPage, query.page())
            && Objects.equals(expectedPerPage, query.perPage())
            && Objects.equals(expectedTerms, query.terms())
            && Objects.equals(expectedSort, query.sort())
            && Objects.equals(expectedDirection, query.direction())
        ));

    }
}