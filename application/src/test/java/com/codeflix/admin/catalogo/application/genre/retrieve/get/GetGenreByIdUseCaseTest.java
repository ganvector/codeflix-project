package com.codeflix.admin.catalogo.application.genre.retrieve.get;

import com.codeflix.admin.catalogo.application.UseCaseTest;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @Mock
    private GenreGateway genreGateway;

    @InjectMocks
    private DefaultGetGenreByIdUseCase getGenreByIdUseCase;


    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void shouldReturnGenreWhenGivenAValidId() {
        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.load("movies"),
                CategoryID.load("series")
        );

        final var aGenre = Genre.createGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        final var actualGenre = getGenreByIdUseCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(expectedId);
    }

    @Test
    public void shouldReturnNotFoundGenreWhenGivenAValidIdThatDoesNotExists() {
        final var expectedErrorMessage = "Genre with ID not_exists was not found";

        final var expectedId = GenreID.load("not_exists");

        Mockito
                .when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> getGenreByIdUseCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito
                .verify(genreGateway, Mockito.times(1))
                .findById(expectedId);
    }

    private List<String> asString (List<CategoryID> genreIDS) {
        return genreIDS.stream().map(CategoryID::getValue).toList();
    }
}
