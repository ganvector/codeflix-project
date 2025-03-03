package com.codeflix.admin.catalogo.e2e.genre;

import com.codeflix.admin.catalogo.E2ETest;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.e2e.MockDsl;
import com.codeflix.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;


    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.2.0")
            .withUsername("root")
            .withPassword("123456")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @BeforeEach
    void cleanUp() {
        genreRepository.deleteAll();
    }

    @Test
    public void shouldBeAbleToCreateAGenreWithValidInputsAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualCategory = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualCategory.getCategories().size() && expectedCategories.containsAll(actualCategory.getCategoryIDs()));
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToCreateAGenreWithCategoriesGivenValidInputsAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", "Universe's best movies", true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualCategory = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualCategory.getCategories().size()
                && expectedCategories.containsAll(actualCategory.getCategoryIDs()));
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToNavigateThroughAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Comedy", true, List.of());

        listGenres(0, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Action")));

        listGenres(1, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Comedy")));

        listGenres(2, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(3, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void shouldBeAbleToSearchThroughAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Comedy", true, List.of());

        listGenres(0, 1, "dra")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));
    }

    @Test
    public void shouldBeAbleToSortByDescriptionDescThroughAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Comedy", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Comedy")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Action")));
    }

    @Test
    public void shouldBeAbleToGetGenreByItsIdentifierAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("Movies", "Universe's best movies", true);

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && expectedCategories.stream().map(CategoryID::getValue).toList().containsAll(actualGenre.categories()));
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.deletedAt());
    }

    @Test
    public void shouldBeAbleToGetATreatErrorWhenGenreIsNotFoundByItsIdentifierAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/genres/not-found")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Genre with ID not-found was not found")));
    }

    @Test
    public void shouldBeAbleToUpdateAGenreByItsIdentifierAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var movies = givenACategory("Movies", "Universe's best movies", true);
        final var actualId = givenAGenre("acction", false, List.of());

        final var expectedName = "Action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var genreRequest = new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories.stream().map(CategoryID::getValue).toList());
        updateGenre(actualId, genreRequest);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(actualGenre.getCreatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToActivateAGenreByItsIdentifierAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var movies = givenACategory("Movies", "Universe's best movies", true);

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, false, List.of(movies));

        final var genreRequest = new UpdateGenreRequest(expectedName, expectedIsActive, expectedCategories.stream().map(CategoryID::getValue).toList());
        updateGenre(actualId, genreRequest);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(actualGenre.getCreatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToDeleteAGenreByItsIdentifierAsAnAdmin() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var actualId = givenAGenre("Action", true, List.of());

        Assertions.assertTrue(genreRepository.existsById(actualId.getValue()));

        deleteGenre(actualId);

        Assertions.assertFalse(genreRepository.existsById(actualId.getValue()));
    }
}