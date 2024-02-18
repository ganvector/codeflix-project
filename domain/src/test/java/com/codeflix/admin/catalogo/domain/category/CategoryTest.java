package com.codeflix.admin.catalogo.domain.category;

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

    @Test
    public void shouldNotRaiseAnErrorWhenDescriptionIsEmpty() {
        final String expectedName = "Movies";
        final var expectedDescription = "   ";
        final var expectedIsActive = true;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
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
    public void shouldNotRaiseAnErrorWhenActiveIsFalse() {
        final String expectedName = "Movies";
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = false;

        final Category category = Category.createCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void shouldInactivateACategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = false;

        final Category originalCategory = Category.createCategory(expectedName, expectedDescription, true);
        final var createdAt = originalCategory.getCreatedAt();
        final var updatedAt = originalCategory.getUpdatedAt();

        Assertions.assertTrue(originalCategory.isActive());
        Assertions.assertNull(originalCategory.getDeletedAt());

        final var category = originalCategory.deactivate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(originalCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(createdAt, category.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void shouldActivateACategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = true;

        final Category originalCategory = Category.createCategory(expectedName, expectedDescription, false);
        final var createdAt = originalCategory.getCreatedAt();
        final var updatedAt = originalCategory.getUpdatedAt();

        Assertions.assertFalse(originalCategory.isActive());
        Assertions.assertNotNull(originalCategory.getDeletedAt());

        final var category = originalCategory.activate();

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(originalCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(createdAt, category.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void shouldUpdateACategory () {
        final String expectedName = "Movies";
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = true;

        final Category originalCategory = Category.createCategory("Film", "Random movies", false);
        final var createdAt = originalCategory.getCreatedAt();
        final var updatedAt = originalCategory.getUpdatedAt();

        Assertions.assertFalse(originalCategory.isActive());
        Assertions.assertNotNull(originalCategory.getDeletedAt());

        final var category = originalCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(originalCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(createdAt, category.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void shouldUpdateACategoryToInactive () {
        final String expectedName = "Movies";
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = false;

        final Category originalCategory = Category.createCategory("Film", "Random movies", true);
        final var createdAt = originalCategory.getCreatedAt();
        final var updatedAt = originalCategory.getUpdatedAt();

        Assertions.assertTrue(originalCategory.isActive());
        Assertions.assertNull(originalCategory.getDeletedAt());

        final var category = originalCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(originalCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(createdAt, category.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void shouldUpdateACategoryToInvalid () {
        final String expectedName = null;
        final var expectedDescription = "Planet's best movies in one place";
        final var expectedIsActive = false;

        final Category originalCategory = Category.createCategory("Film", "Random movies", true);
        final var createdAt = originalCategory.getCreatedAt();
        final var updatedAt = originalCategory.getUpdatedAt();

        Assertions.assertTrue(originalCategory.isActive());
        Assertions.assertNull(originalCategory.getDeletedAt());

        final var category = originalCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(originalCategory.getId(), category.getId());
        Assertions.assertNull(category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertEquals(createdAt, category.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(category.getDeletedAt());
    }
}
