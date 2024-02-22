package com.codeflix.admin.catalogo.application.genre.update;

import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultUpdateGenreUseCase updateGenreUseCase;

    @Test
    public void shouldUpdateAGenreWhenGivenAValidCommand() {
        final var aGenre = Genre.createGenre("Action", true);
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

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito
                .when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(Mockito.eq(expectedId));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .update(Mockito.argThat(anUpdatedGenre ->
                        Objects.equals(expectedId, anUpdatedGenre.getId())
                        && Objects.equals(expectedName, anUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                        && anUpdatedGenre.getUpdatedAt().isAfter(anUpdatedGenre.getCreatedAt())
                        && Objects.isNull(anUpdatedGenre.getDeletedAt())
                ));
    }

    @Test
    public void shouldUpdateAGenreToInactiveWhenGivenAValidCommand() {
        final var aGenre = Genre.createGenre("Action", true);
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

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito
                .when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(Mockito.eq(expectedId));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .update(Mockito.argThat(anUpdatedGenre ->
                        Objects.equals(expectedId, anUpdatedGenre.getId())
                                && Objects.equals(expectedName, anUpdatedGenre.getName())
                                && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                                && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                                && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                                && anUpdatedGenre.getUpdatedAt().isAfter(anUpdatedGenre.getCreatedAt())
                                && Objects.nonNull(anUpdatedGenre.getDeletedAt())
                ));
    }

    @Test
    public void shouldUpdateAGenreWhenGivenAValidCommandWithCategories() {
        final var aGenre = Genre.createGenre("Action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(
                CategoryID.load("123"),
                CategoryID.load("456")
        );

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito
                .when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito
                .when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = updateGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(Mockito.eq(expectedId));

        Mockito
                .verify(categoryGateway, Mockito.times(1))
                .existsByIds(Mockito.eq(expectedCategories));

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .update(Mockito.argThat(anUpdatedGenre ->
                        Objects.equals(expectedId, anUpdatedGenre.getId())
                                && Objects.equals(expectedName, anUpdatedGenre.getName())
                                && Objects.equals(expectedIsActive, anUpdatedGenre.isActive())
                                && Objects.equals(expectedCategories, anUpdatedGenre.getCategories())
                                && Objects.equals(aGenre.getCreatedAt(), anUpdatedGenre.getCreatedAt())
                                && anUpdatedGenre.getUpdatedAt().isAfter(anUpdatedGenre.getCreatedAt())
                                && Objects.isNull(anUpdatedGenre.getDeletedAt())
                ));
    }

    @Test
    public void shouldReturnDomainExceptionWhenUpdateGenreHasANullName() {
        final var aGenre = Genre.createGenre("Action", true);
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

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> updateGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenUpdatingGenreSomeCategoryDoesNotExistsAndHasAInvalidName() {
        final var aGenre = Genre.createGenre("Action", true);
        final var expectedId = aGenre.getId();
        final var movies = CategoryID.load("movies");
        final var series = CategoryID.load("series");
        final var anime = CategoryID.load("anime");

        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, anime);

        final var expectedErrorCount = 2;
        final var expectedCategoriesErrorMessage = "Some categories could not be found: movies, anime";
        final var expectedGenreErrorMessage = "'name' should not be empty";

        final var aCommand = UpdateGenreCommand.create(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito
                .when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(List.of(series));

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
}
