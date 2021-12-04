package com.bs.search.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagingRepository extends JpaRepository<BookEntity, Long> {

     Page<BookEntity> findAll(Pageable pageable);
     Optional<Page<BookEntity>> findAllByTitleLike(String title, Pageable pageable);
     Optional<Page<BookEntity>> findAllByPriceBetween(long min,long max, Pageable pageable);


}
