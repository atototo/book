package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TranslatorsRepository extends CrudRepository<TranslatorsEntity, Long> {

    List<TranslatorsEntity> findAllByTitle(String title);
}