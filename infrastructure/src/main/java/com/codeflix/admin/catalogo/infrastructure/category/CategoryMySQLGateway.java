package com.codeflix.admin.catalogo.infrastructure.category;

import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;
import com.codeflix.admin.catalogo.domain.category.CategorySearchQuery;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.codeflix.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return this.save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anCategoryID) {
        final var idValue = anCategoryID.getValue();
        if (this.repository.existsById(idValue)) {
            this.repository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anCategoryID) {
        return this.repository.findById(anCategoryID.getValue()).map(CategoryJPAEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return this.save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery sQuery) {
        return null;
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJPAEntity.create(aCategory)).toAggregate();
    }
}
