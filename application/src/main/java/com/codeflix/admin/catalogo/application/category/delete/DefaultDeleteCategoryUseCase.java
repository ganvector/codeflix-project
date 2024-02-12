package com.codeflix.admin.catalogo.application.category.delete;

import com.codeflix.admin.catalogo.domain.category.CategoryGateway;
import com.codeflix.admin.catalogo.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public void execute(String anId) {
        this.categoryGateway.deleteById(CategoryID.load(anId));
    }
}
