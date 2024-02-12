package com.codeflix.admin.catalogo.domain.validation.handlers;

import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError) {
        return new Notification(List.of(anError));
    }

    public static Notification create(final Throwable t) {
        return new Notification(List.of(new Error(t.getMessage())));
    }

    @Override
    public ValidationHandler append(final Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        try {
            aValidation.validate();
        } catch (DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
