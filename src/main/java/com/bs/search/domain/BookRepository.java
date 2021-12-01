package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<BookEntity, Long> {

    List<BookEntity> findAllByTitle(String name);

    List<BookEntity> findAllByPriceBetween(long min, long max);
}
