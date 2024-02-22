package com.codeflix.admin.catalogo.application.genre.create;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.List;
import java.util.Objects;

public class CreateGenreUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultCreateGenreUseCase createGenreUseCase;

    public List<Object> getMocks() {
        return List.of(genreGateway, categoryGateway);
    }

    @Test
    public void shouldReturnGenreIdWhenCallCreateGenreWithValidInput() {
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito
                .when(genreGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1))
                .create(Mockito.argThat(arg ->
                        Objects.equals(expectedName, arg.getName())
                        && Objects.equals(expectedIsActive, arg.isActive())
                        && Objects.equals(expectedCategories, arg.getCategories())
                        && Objects.nonNull(arg.getCreatedAt())
                        && Objects.nonNull(arg.getUpdatedAt())
                        && Objects.isNull(arg.getDeletedAt())
                ));
    }

    @Test
    public void shouldReturnAnInactiveGenreIdWhenCallCreateGenreWithValidInput() {
        final var expectedName = "Horror";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito
                .when(genreGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1))
                .create(Mockito.argThat(arg ->
                        Objects.equals(expectedName, arg.getName())
                                && Objects.equals(expectedIsActive, arg.isActive())
                                && Objects.equals(expectedCategories, arg.getCategories())
                                && Objects.nonNull(arg.getCreatedAt())
                                && Objects.nonNull(arg.getUpdatedAt())
                                && Objects.nonNull(arg.getDeletedAt())
                ));
    }

    @Test
    public void shouldReturnGenreIdWhenGivenAValidCommandWithCategories() {
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.load("123"),
                CategoryID.load("456")
        );

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito
                .when(genreGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Mockito
                .when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        final var actualOutput = createGenreUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                        .existsByIds(expectedCategories);

        Mockito.verify(genreGateway, Mockito.times(1))
                .create(Mockito.argThat(arg ->
                        Objects.equals(expectedName, arg.getName())
                                && Objects.equals(expectedIsActive, arg.isActive())
                                && Objects.equals(expectedCategories, arg.getCategories())
                                && Objects.nonNull(arg.getCreatedAt())
                                && Objects.nonNull(arg.getUpdatedAt())
                                && Objects.isNull(arg.getDeletedAt())
                ));
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
        final var movies = CategoryID.load("movies");
        final var series = CategoryID.load("series");
        final var anime = CategoryID.load("anime");

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, anime);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Some categories could not be found: movies, anime";

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito
                .when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(List.of(series));

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> createGenreUseCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void shouldReturnDomainExceptionWhenSomeCategoryDoesNotExistsAndHasAInvalidName() {
        final var movies = CategoryID.load("movies");
        final var series = CategoryID.load("series");
        final var anime = CategoryID.load("anime");

        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, anime);

        final var expectedErrorCount = 2;
        final var expectedCategoriesErrorMessage = "Some categories could not be found: movies, anime";
        final var expectedGenreErrorMessage = "'name' should not be empty";

        final var aCommand = CreateGenreCommand.create(expectedName, expectedIsActive, asString(expectedCategories));

        Mockito
                .when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(List.of(series));

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


}
