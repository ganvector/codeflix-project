package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.IntegrationTest;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@IntegrationTest
public class UpdateGenreUseCaseIT {
    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private UpdateGenreUseCase updateGenreUseCase;

    @Test
    public void shouldUpdateAGenreWhenGivenAValidCommand() {
        final var aGenre = Genre.createGenre("Action", true);
        genreGateway.create(aGenre);
        final var expectedId = aGenre.getId();
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

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
    public void shouldUpdateAGenreToInactiveWhenGivenAValidCommand() {
        final var aGenre = Genre.createGenre("Action", true);
        genreGateway.create(aGenre);
        final var expectedId = aGenre.getId();
        final var expectedName = "Horror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );


        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sortCategoriesIds(expectedCategories), sortCategoriesIds(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldUpdateAGenreWhenGivenAValidCommandWithCategories() {
        final var movies = Category.createCategory("Movies", null, true);
        final var series = Category.createCategory("Series", null, true);
        final var anime = Category.createCategory("Anime", null, true);
        categoryGateway.create(movies);
        categoryGateway.create(series);
        categoryGateway.create(anime);
        final var aGenre = Genre.createGenre("Action", true);
        genreGateway.create(aGenre);
        final var expectedId = aGenre.getId();
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                movies.getId(),
                series.getId(),
                anime.getId()
        );

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

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
    public void shouldReturnDomainExceptionWhenUpdateGenreHasANullName() {
        final var aGenre = Genre.createGenre("Action", true);
        genreGateway.create(aGenre);
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> updateGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenUpdatingGenreSomeCategoryDoesNotExistsAndHasAInvalidName() {
        final Category series = Category.createCategory("Series", null, true);
        categoryGateway.create(series);
        final var aGenre = Genre.createGenre("Action", true);
        genreGateway.create(aGenre);
        final var expectedId = aGenre.getId();

        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.load("movies"), series.getId(), CategoryID.load("anime"));

        final var expectedErrorCount = 2;
        final var expectedCategoriesErrorMessage = "Some categories could not be found: movies, anime";
        final var expectedGenreErrorMessage = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> updateGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedCategoriesErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedGenreErrorMessage, actualException.getErrors().get(1).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
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
