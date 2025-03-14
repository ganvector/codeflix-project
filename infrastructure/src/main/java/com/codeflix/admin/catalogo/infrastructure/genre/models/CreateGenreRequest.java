package com.codeflix.admin.catalogo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record CreateGenreRequest(
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories
) {

    public List<String> categories() {
        return this.categories != null ? this.categories : Collections.emptyList();
    }

    public Boolean active() {
        return this.active != null ? this.active : true;
    }
}
