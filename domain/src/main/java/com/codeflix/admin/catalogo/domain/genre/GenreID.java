package com.codeflix.admin.catalogo.domain.genre;

import com.codeflix.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreID generateUnique() {
        return GenreID.load(UUID.randomUUID());
    }

    public static GenreID load(final String anId) {
        return new GenreID(anId);
    }

    public static GenreID load(final UUID anId) {
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
