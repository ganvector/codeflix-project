package com.codeflix.admin.catalogo.application.category.delete;

import com.codeflix.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase deleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void shouldDeleteACategoryWhenInputIdIsValid() {
        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);
        final var expectedId = aCategory.getId();

        Mockito
                .doNothing()
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void shouldReturnsSuccessfullyWhenInputIdIsNotValid() {
        final var expectedId = CategoryID.load("123");

        Mockito
                .doNothing()
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void shouldThrowAnErrorWhenGatewayThrows() {
        final var expectedId = CategoryID.load("123");

        Mockito
                .doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }
}
