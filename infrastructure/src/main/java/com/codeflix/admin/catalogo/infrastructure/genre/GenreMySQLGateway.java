package com.codeflix.admin.catalogo.infrastructure.genre;

import com.codeflix.admin.catalogo.domain.genre.Genre;
import com.codeflix.admin.catalogo.domain.genre.GenreGateway;
import com.codeflix.admin.catalogo.domain.genre.GenreID;
import com.codeflix.admin.catalogo.domain.pagination.Pagination;
import com.codeflix.admin.catalogo.domain.pagination.SearchQuery;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.codeflix.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return this.save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var aGenreId = anId.getValue();
        if (this.genreRepository.existsById(aGenreId)) {
            this.genreRepository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return this.save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var results =
                this.genreRepository.findAll(Specification.where(where), page);

        return new Pagination<>(
                results.getNumber(),
                results.getSize(),
                results.getTotalElements(),
                results.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(Iterable<GenreID> genreIDS) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.load(aGenre)).toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
