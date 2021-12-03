package com.bs.search.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagingRepository extends JpaRepository<BookEntity, Long> {

     Page<BookEntity> findAll(Pageable pageable);
     Page<BookEntity> findAllByTitle(String title, Pageable pageable);
//     Page<BookEntity> findAllB
     Page<BookEntity> findAllByPriceBetween(long min,long max, Pageable pageable);

}
