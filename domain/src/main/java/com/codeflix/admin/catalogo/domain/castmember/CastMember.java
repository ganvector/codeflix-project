package com.codeflix.admin.catalogo.domain.castmember;

import com.codeflix.admin.catalogo.domain.AggregateRoot;
import com.codeflix.admin.catalogo.domain.exceptions.NotificationException;
import com.codeflix.admin.catalogo.domain.utils.InstantUtils;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.handlers.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        validate();
    }

    public static CastMember create(final String aName, final CastMemberType aType) {
        final var anId = CastMemberID.generateUnique();
        final var now = InstantUtils.now();
        return new CastMember(anId, aName, aType, now, now);
    }

    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        return new CastMember(anId, aName, aType, aCreationDate, aUpdateDate);
    }

    public static CastMember from(final CastMember aMember) {
        return new CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
        );
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        validate();
        return this;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void validate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }
}