package com.codeflix.admin.catalogo.application.category.retrieve.get;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Test
    public void shouldGetACategoryWhenInputIdIsValid() {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;
        final var aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        final var output = getCategoryByIdUseCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), output.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), output.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), output.deletedAt());
    }

    @Test
    public void shouldReturnsSuccessfullyWhenInputIdIsNotValid() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;
        final var expectedId = CategoryID.load("123");

        final var actualException = Assertions.assertThrows(
                NotFoundException.class, () -> getCategoryByIdUseCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldThrowAnErrorWhenGatewayThrows() {
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = CategoryID.load("123");

        Mockito.doThrow(new IllegalStateException("Gateway Error")).when(categoryGateway).findById(Mockito.eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class, () -> getCategoryByIdUseCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
