package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorsRepository extends CrudRepository<AuthorsEntity, Long> {

    List<AuthorsEntity> findAllByTitle(String title);
}
