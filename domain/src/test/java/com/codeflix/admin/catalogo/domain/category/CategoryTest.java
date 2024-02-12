package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
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
    public void shouldRaiseAnErrorWhenNameIsNull() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var raisedException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, raisedException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, raisedException.getErrors().get(0).message());
    }

    @Test
    public void shouldRaiseAnErrorWhenNameIsEmpty() {
        final String expectedName = "";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var raisedException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, raisedException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, raisedException.getErrors().get(0).message());
    }

    @Test
    public void shouldRaiseAnErrorWhenNameIsLessThan3() {
        final String expectedName = "Na ";
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var raisedException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, raisedException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, raisedException.getErrors().get(0).message());
    }

    @Test
    public void shouldRaiseAnErrorWhenNameIsMoreThan255() {
        final String expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu finibus turpis, sed mollis massa. 
                Ut hendrerit risus leo, ac luctus ipsum tempor ut. Etiam in justo ac odio rhoncus ultrices. 
                Cras non dui eget orci sodales posuere. Aliquam sagittis sed risus ut lobortis. Fusce sagittis semper 
                augue.
                """;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);
        final var raisedException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, raisedException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, raisedException.getErrors().get(0).message());
    }
}
