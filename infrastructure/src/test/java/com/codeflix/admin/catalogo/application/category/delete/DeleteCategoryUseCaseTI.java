package com.codeflix.admin.catalogo.application.category.delete;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class DeleteCategoryUseCaseTI {
    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() { categoryRepository.deleteAll(); }

    @Test
    public void shouldDeleteACategoryWhenInputIdIsValid() {
        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);
        final var expectedId = aCategory.getId();

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void shouldReturnsSuccessfullyWhenInputIdIsNotValid() {
        final var expectedId = CategoryID.load("123");
        final var aCategory = Category.createCategory("Movies", "Universe's best movies", true);

        categoryRepository.saveAndFlush(CategoryJPAEntity.create(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, categoryRepository.count());
    }

    @Test
    public void shouldThrowAnErrorWhenGatewayThrows() {
        final var expectedId = CategoryID.load("123");

        Mockito
                .doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> deleteCategoryUseCase.execute(expectedId.getValue()));
    }
}
