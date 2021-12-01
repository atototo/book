package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorsRepository extends CrudRepository<Authors, Long> {

    List<Authors> findAllByTitle(String title);
}
