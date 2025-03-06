package com.codeflix.admin.catalogo.application;

import com.codeflix.admin.catalogo.domain.castmember.CastMember;
import com.codeflix.admin.catalogo.domain.castmember.CastMemberType;
import com.codeflix.admin.catalogo.domain.category.Category;
import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.video.Rating;
import com.codeflix.admin.catalogo.domain.video.Resource;
import com.github.javafaker.Faker;
import io.vavr.API;

import java.awt.*;
import java.time.Year;
import java.util.List;

import static io.vavr.API.$;

public class Fixture {
    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year () {
        return FAKER.random().nextInt(1984, Year.now().getValue());
    }

    public static Boolean bool() {
        return FAKER.bool().bool();
    }

    public static class CastMembers {
        private static final CastMember KURT_RUSSEL = CastMember.create("Kurt Russel", CastMemberType.ACTOR);
        private static final CastMember TONI_COLLETTE = CastMember.create("Toni Collette", CastMemberType.ACTOR);
        private static final CastMember ANYA_TAYLOR_JOY = CastMember.create("Anya Taylor-Joy", CastMemberType.ACTOR);

        private static final CastMember JOHN_CARPENTER = CastMember.create("John Carpenter", CastMemberType.DIRECTOR);
        private static final CastMember FRANK_DARABONT =  CastMember.create("Frank Darabont", CastMemberType.DIRECTOR);
        private static final CastMember JORDAN_PEELE = CastMember.create("Jordan Peel", CastMemberType.DIRECTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }
    }

    public static class Categories {
        private static final Category DOCUMMENTARY = Category.createCategory("Docummentary", "World's chilling docs", true);
        private static final Category MOVIES = Category.createCategory("Movies", "The best movies on the planet", true);
    }

    public static class Genres {
        private static final Genre TERROR = Genre.createGenre("Terror", true);
        private static final Genre HORROR = Genre.createGenre("Horror", true);
        private static final Genre THRILLER = Genre.createGenre("THRILLER", true);
    }

    public static class Videos {
        public static String title() {
            return FAKER.options().option("The Witch", "Hereditary", "Get Out", "Midsommar", "The Conjuring", "The Cabin in the Woods", "The Mist", "The Thing", "Sinister", "It Follows");
        }

        public static String description() {
            return FAKER.lorem().characters(40, 254);
        }

        public static Double duration() {
            return FAKER.random().nextInt(68, 190).doubleValue();
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static CastMember castMember() {
            return FAKER.options().option(
                    CastMember.from(CastMembers.ANYA_TAYLOR_JOY),
                    CastMember.from(CastMembers.FRANK_DARABONT),
                    CastMember.from(CastMembers.JOHN_CARPENTER),
                    CastMember.from(CastMembers.KURT_RUSSEL),
                    CastMember.from(CastMembers.JORDAN_PEELE),
                    CastMember.from(CastMembers.TONI_COLLETTE)
            );
        }

        public static Category category() {
            return FAKER.options().option(
                    Categories.DOCUMMENTARY.clone(),
                    Categories.MOVIES.clone()
            );
        }

        public static Genre genre() {
            return FAKER.options().option(
                    Genres.HORROR.clone(),
                    Genres.TERROR.clone(),
                    Genres.THRILLER.clone()
            );
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = API.Match(type).of(
                    API.Case(API.$(API.List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    API.Case(API.$(), "image/jpg")
            );

            final byte[] content = FAKER.lorem().characters(300, 1000).getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }
    }
}
