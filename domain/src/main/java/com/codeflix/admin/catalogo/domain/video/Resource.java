package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {
    private final byte[] content;
    private final String contentType;
    private final String name;
    private final Type type;

    public enum Type {
        VIDEO,
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAIL_HALF
    }

    private Resource(final byte[] content, final String contentType, final String name, final Type type) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public static Resource with(final byte[] content, final String contentType, final String name, final Type type) {
        return new Resource(content, contentType, name, type);
    }
}
