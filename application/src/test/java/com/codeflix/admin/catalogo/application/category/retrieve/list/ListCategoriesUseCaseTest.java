package com.codeflix.admin.catalogo.application.category.retrieve.list;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategorySearchQuery;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase listCategoriesUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    @DisplayName("should return categories when query is valid")
    public void shouldReturnCategoriesWhenQueryIsValid() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";


        final var aQuery = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        final var categories = List.of(
                Category.createCategory("Movies", null, true),
                Category.createCategory("Series", null, true)
        );

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(ListCategoriesOutput::create);

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery))).thenReturn(expectedPagination);

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedResult, output);
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(categories.size(), output.total());
    }

    @Test
    @DisplayName("should return empty categories when has no results for the query")
    public void shouldReturnEmptyCategoriesWhenHasNoResultsForTheQuery() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";


        final var aQuery = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        final var categories = List.<Category>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(ListCategoriesOutput::create);

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery))).thenReturn(expectedPagination);

        final var output = listCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, output.items().size());
        Assertions.assertEquals(expectedResult, output);
        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(categories.size(), output.total());
    }

    @Test
    @DisplayName("Should return an exception when gateway throws")
    public void shouldReturnAnExceptionWhenGatewayThrows() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway Error";

        final var aQuery = new CategorySearchQuery(
                expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection
        );

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> listCategoriesUseCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
