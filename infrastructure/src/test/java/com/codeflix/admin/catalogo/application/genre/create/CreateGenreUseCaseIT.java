package com.codeflix.admin.catalogo.application.genre.create;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private DefaultCreateGenreUseCase createGenreUseCase;

    @Test
    public void shouldReturnGenreIdWhenCallCreateGenreWithValidInput() {
        final var movies = Category.createCategory("Movies", null, true);
        categoryGateway.create(movies);

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId());

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void shouldReturnAnInactiveGenreIdWhenCallCreateGenreWithValidInput() {
        final var expectedName = "Horror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldReturnGenreIdWhenGivenAValidCommandWithCategories() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        final var anime = Category.createCategory("Anime", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);
        categoryGateway.create(anime);
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                movies.getId(), series.getId(), anime.getId()
        );

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldReturnDomainExceptionWhenGenreHasAnInvalidName() {
        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> createGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenGenreHasANullName() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> createGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenSomeCategoryDoesNotExists() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        final var anime = Category.createCategory("Anime", null, true);
        categoryGateway.create(series);

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId(), anime.getId());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some categories could not be found: %s, %s".formatted(
                movies.getId().getValue(), anime.getId().getValue()
        );

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> createGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenSomeCategoryDoesNotExistsAndHasAInvalidName() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        final var anime = Category.createCategory("Anime", null, true);
        categoryGateway.create(series);

        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.getId(), series.getId(), anime.getId());

        final var expectedErrorCount = 2;
        final var expectedCategoriesErrorMessage = "Some categories could not be found: %s, %s".formatted(
                movies.getId().getValue(), anime.getId().getValue()
        );
        final var expectedGenreErrorMessage = "'name' should not be empty";

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> createGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedCategoriesErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedGenreErrorMessage, actualException.getErrors().get(1).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    private List<String> asString(final List<CategoryID> categoryIDS) {
        return categoryIDS.stream()
                .map(CategoryID::getValue)
                .toList();
    }

    private static List<CategoryID> sortCategoriesIds(List<CategoryID> expectedCategories) {
        return expectedCategories.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }
}
