package com.codeflix.admin.catalogo.domain.genre;

import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.validation.handlers.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
