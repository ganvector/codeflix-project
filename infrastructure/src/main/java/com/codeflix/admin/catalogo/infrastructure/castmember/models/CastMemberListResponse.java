package com.codeflix.admin.catalogo.infrastructure.castmember.models;

public record CastMemberListResponse(
        String id,
        String name,
        String type,
        String createdAt
) {
}
