package com.codeflix.admin.catalogo.application.category.retrieve.get;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase getCategoryByIdUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    @DisplayName("should get a category when category id is valid")
    public void shouldGetACategoryWhenInputIdIsValid() {
        final var expectedName = "Movies";
        final var expectedDescription = "Universe's best movies";
        final var expectedIsActive = true;
        final var aCategory = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        Mockito
                .when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

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
    @DisplayName("should return not found when category id is not valid")
    public void shouldReturnsSuccessfullyWhenInputIdIsNotValid() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;
        final var expectedId = CategoryID.load("123");

        Mockito
                .when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class, () -> getCategoryByIdUseCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    @DisplayName("should return an exception when gateway throws exception")
    public void shouldThrowAnErrorWhenGatewayThrows() {
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = CategoryID.load("123");

        Mockito
                .when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class, () -> getCategoryByIdUseCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
