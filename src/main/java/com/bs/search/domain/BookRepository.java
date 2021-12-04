package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<BookEntity, Long> {

    Optional<List<BookEntity>> findByTitleContaining(String name);

    List<BookEntity> findAllByPriceBetween(long min, long max);

    List<BookEntity> findAllById(Long id);
}
