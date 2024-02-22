package com.codeflix.admin.catalogo.domain.genre;

import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void shouldInstantiateANewGenreWhenGivenValidParameters () {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoriesSize = 0;

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(aGenre.getId());
        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, aGenre.getCategories().size());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertNotNull(aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldThrowAnErrorWhenGivenNoName () {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions
                .assertThrows(NotificationException.class, () -> Genre.createGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldThrowAnErrorWhenGivenAnInvalidName () {
        final String expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions
                .assertThrows(NotificationException.class, () -> Genre.createGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldThrowAnErrorWhenGivenANameBiggerThan255Characters () {
        final String expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer eu finibus turpis, sed mollis massa.\s
                Ut hendrerit risus leo, ac luctus ipsum tempor ut. Etiam in justo ac odio rhoncus ultrices.\s
                Cras non dui eget orci sodales posuere. Aliquam sagittis sed risus ut lobortis. Fusce sagittis semper\s
                augue.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions
                .assertThrows(NotificationException.class, () -> Genre.createGenre(expectedName, expectedIsActive));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldInactivateAnActiveGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = false;

        final var aGenre = Genre.createGenre(expectedName, true);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.deactivate();

        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNotNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldActiveAnInactiveGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;

        final var aGenre = Genre.createGenre(expectedName, false);

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        aGenre.activate();

        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAGenreWithValidParameters () {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategoriesSize = 1;
        final var expectedCategories = List.of(CategoryID.load("123"));

        final var aGenre = Genre.createGenre("Horror", true);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, aGenre.getCategories().size());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNotNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAndActivateAGenreWithValidParameters () {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoriesSize = 1;
        final var expectedCategories = List.of(CategoryID.load("123"));

        final var aGenre = Genre.createGenre("Horror", false);

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, aGenre.getCategories().size());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAndDeactivateAGenreWithValidParameters () {
        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategoriesSize = 1;
        final var expectedCategories = List.of(CategoryID.load("123"));

        final var aGenre = Genre.createGenre("Horror", true);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, aGenre.getCategories().size());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNotNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldReceiveExceptionWhenUpdatingWithNoName () {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.load("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aGenre = Genre.createGenre("Horror", true);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var actualException = Assertions
                .assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldReceiveExceptionWhenUpdatingWithEmptyName () {
        final String expectedName = "   ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.load("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aGenre = Genre.createGenre("Horror", true);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var actualException = Assertions
                .assertThrows(NotificationException.class, () -> aGenre.update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void shouldUpdateAGenreWithNullCategories() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoriesSize = 0;
        final var expectedCategoryList = new ArrayList<CategoryID>();

        final var aGenre = Genre.createGenre("Horror", expectedIsActive);

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertEquals(aGenre.getCreatedAt(), aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());

        aGenre.update(expectedName, expectedIsActive, null);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoriesSize, aGenre.getCategories().size());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldAddANewCategoryToEmptyCategoryAGenre() {
        CategoryID seriesID = CategoryID.load("series");
        CategoryID moviesID = CategoryID.load("movies");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.of(seriesID, moviesID);

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, aGenre.getCategories().size());

        aGenre.addCategory(seriesID);
        aGenre.addCategory(moviesID);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldAddANewCategoriesToEmptyCategoryAGenre() {
        CategoryID seriesID = CategoryID.load("series");
        CategoryID moviesID = CategoryID.load("movies");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.of(seriesID, moviesID);

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, aGenre.getCategories().size());

        aGenre.addCategories(expectedCategoryList);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldAddAnEmptyCategoriesToEmptyCategoryAGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.<CategoryID>of();

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, aGenre.getCategories().size());

        aGenre.addCategories(expectedCategoryList);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertNotNull(aGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldRemoveANewCategoryFromAGenre() {
        CategoryID seriesID = CategoryID.load("series");
        CategoryID moviesID = CategoryID.load("movies");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.of(moviesID);

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);
        aGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, aGenre.getCategories().size());

        aGenre.removeCategory(seriesID);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldDoNothingWhenAddingANullCategoryToEmptyCategoryAGenre() {
        CategoryID moviesID = CategoryID.load("movies");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.of(moviesID);

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, aGenre.getCategories().size());

        aGenre.addCategory(null);
        aGenre.addCategory(moviesID);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    public void shouldDoNothingWhenRemovingANullCategoryToEmptyCategoryAGenre() {
        CategoryID seriesID = CategoryID.load("series");
        CategoryID moviesID = CategoryID.load("movies");

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategoryList = List.of(seriesID, moviesID);

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive);
        aGenre.update(expectedName, expectedIsActive, expectedCategoryList);

        Assertions.assertEquals(2, aGenre.getCategories().size());

        aGenre.removeCategory(null);

        Assertions.assertEquals(expectedName, aGenre.getName());
        Assertions.assertEquals(expectedIsActive, aGenre.isActive());
        Assertions.assertEquals(expectedCategoryList, aGenre.getCategories());
        Assertions.assertNotNull(aGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isAfter(aGenre.getCreatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());
    }
}
