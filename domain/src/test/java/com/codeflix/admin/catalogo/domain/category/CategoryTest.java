package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void shouldInstantiateAValidCategory() {
        final var expectedName = "Category Name";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void shouldRaiseAnErrorWhenNameIsNotValid() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        final var raisedException = Assertions.assertThrows(DomainException.class, () -> category.validate());
        Assertions.assertEquals(expectedErrorCount, raisedException.getErrors().size);
        Assertions.assertEquals(expectedErrorMessage, raisedException.getErrors().get(0).getMessage());
    }
}
