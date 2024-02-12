package com.codeflix.admin.catalogo.domain;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID identifier) {
        super(identifier);
    }
}
