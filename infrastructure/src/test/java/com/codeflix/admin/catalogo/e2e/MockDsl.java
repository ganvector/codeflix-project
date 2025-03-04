package com.codeflix.admin.catalogo.e2e;

import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberID;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codeflix.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.codeflix.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.admin.catalogo.infrastructure.config.json.Json;
import com.codeflix.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.codeflix.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

public interface MockDsl {

    MockMvc mvc();

//    === CATEGORY ===
    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.load(actualId);
    }

    default CategoryResponse retrieveACategory(final CategoryID anId) throws Exception {
        return this.retrieve("/categories/", anId,  CategoryResponse.class);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return list("/categories", page, perPage, search, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return list("/categories", page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String dir) throws Exception {
        return list("/categories", page, perPage, search, sort, dir);
    }


    default ResultActions updateCategory(final CategoryID anId, final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new UpdateCategoryRequest(aName, aDescription, isActive);
        return this.update("/categories/", anId, aRequestBody);
    }

    default ResultActions deleteCategory(final CategoryID anId) throws Exception {
        return this.delete("/categories/", anId);
    }

//  === GENRE ===

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, isActive, categories.stream().map(CategoryID::getValue).toList());
        final var actualId = this.given("/genres", aRequestBody);
        return GenreID.load(actualId);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return list("/genres", page, perPage, search, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return list("/genres", page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort, final String dir) throws Exception {
        return list("/genres", page, perPage, search, sort, dir);
    }

    default GenreResponse retrieveGenre(final GenreID anId) throws Exception {
        return this.retrieve("/genres/", anId,  GenreResponse.class);
    }

    default ResultActions updateGenre(final GenreID anId, final UpdateGenreRequest body) throws Exception {
        return this.update("/genres/", anId, body);
    }

    default ResultActions deleteGenre(final GenreID anId) throws Exception {
        return this.delete("/genres/", anId);
    }

//    === CAST MEMBERS ===
    default ResultActions deleteACastMember(final CastMemberID anId) throws Exception {
        return this.delete("/cast_members/", anId);
    }

    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        final var actualId = this.given("/cast_members", aRequestBody);
        return CastMemberID.load(actualId);
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return this.givenResult("/cast_members", aRequestBody);
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default CastMemberResponse retrieveACastMember(final CastMemberID anId) throws Exception {
        return this.retrieve("/cast_members/", anId, CastMemberResponse.class);
    }

    default ResultActions retrieveACastMemberResult(final CastMemberID anId) throws Exception {
        return this.retrieveResult("/cast_members/", anId);
    }

    default ResultActions updateACastMember(final CastMemberID anId, final String aName, final CastMemberType aType) throws Exception {
        return this.update("/cast_members/", anId, new UpdateCastMemberRequest(aName, aType));
    }

//  === PRIVATE METHODS ===

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location").replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest);
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String dir) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", dir)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object requestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(requestBody));

        return this.mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
