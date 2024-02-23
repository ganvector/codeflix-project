package com.codeflix.admin.catalogo.application.genre.retrieve.list;

import com.codeflix.admin.catalogo.application.UseCase;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;

abstract public class ListGenreUseCase extends UseCase<SearchQuery, Pagination<ListGenreOutput>> {
}
