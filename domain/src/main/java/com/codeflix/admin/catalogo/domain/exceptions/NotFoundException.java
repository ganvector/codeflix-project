package com.codeflix.admin.catalogo.domain.exceptions;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.Identifier;
import com.codeflix.admin.catalogo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{
    protected NotFoundException(final String aMessage, final List<Error> errors) {
        super(aMessage, errors);
    }

    public static NotFoundException raise(
            final Class<? extends AggregateRoot<?>> anAggregateRoot,
            final Identifier id
    ) {
        final var anError = "%s with ID %s was not found".formatted(anAggregateRoot.getSimpleName(), id.getValue());
        return new NotFoundException(anError, List.of(new Error(anError)));
    }
}
