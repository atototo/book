package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAllByTitle(String name);

    List<Book> findAllByPriceBetween(long min, long max);
}
