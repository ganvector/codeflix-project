package com.codeflix.admin.catalogo.domain.video;

import com.codeflix.admin.catalogo.domain.validation.Error;
import com.codeflix.admin.catalogo.domain.validation.ValidationHandler;
import com.codeflix.admin.catalogo.domain.validation.Validator;

import java.time.Year;
import java.util.Objects;

public class VideoValidator extends Validator {
    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4_000;

    private final Video video;

    public VideoValidator(final Video aVideo, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = Objects.requireNonNull(aVideo);
    }

    @Override
    public void validate() {
        checkTitleConstraint();
        checkDescriptionConstraint();
        checkLaunchedAtConstraint();
        checkRatingConstraint();
    }

    private void checkTitleConstraint() {
        final String title = video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        if (title.length() > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error("'title' should be between 1 and " +  TITLE_MAX_LENGTH + " characters"));
        }
    }

    private void checkDescriptionConstraint() {
        final String description = video.getDescription();

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
        }

        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error("'description' should be between 1 and " +  DESCRIPTION_MAX_LENGTH + " characters"));
        }
    }

    private void checkLaunchedAtConstraint() {
        final Year launchedAt = video.getLaunchedAt();

        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraint() {
        final Rating rating = video.getRating();
        if (rating == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
