package com.codeflix.admin.catalogo.domain.category;

import com.codeflix.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    void deleteById(CategoryID anCategoryID);

    Optional<Category> findById(CategoryID anCategoryID);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery sQuery);
}