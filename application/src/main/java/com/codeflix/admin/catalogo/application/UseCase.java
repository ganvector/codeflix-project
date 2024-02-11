package com.codeflix.admin.catalogo.application;

import com.codeflix.admin.catalogo.domain.Category;

public class UseCase {
    public Category execute() {
        return new Category();
    }
}