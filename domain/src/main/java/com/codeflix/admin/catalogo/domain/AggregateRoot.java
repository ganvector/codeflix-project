package com.codeflix.admin.catalogo.domain;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID identifier) {
        super(identifier);
    }
}
