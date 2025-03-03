package com.codeflix.admin.catalogo.domain.castmember;

import com.codeflix.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CastMemberID generateUnique() {
        return CastMemberID.load(UUID.randomUUID());
    }

    public static CastMemberID load(final String anId) {
        return new CastMemberID(anId);
    }

    public static CastMemberID load(final UUID anId) {
        return new CastMemberID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
