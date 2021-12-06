package com.bs.search.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : com.bs.search.domain
 * fileName : PagingBookRepository
 * author : yelee
 * date : 2021-12-04
 * description : 도서 정보 페이징 용도 repository
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public interface PagingBookRepository extends JpaRepository<BookEntity, Long> {

     Page<BookEntity> findByTitleContaining(String title, Pageable pageable);
     Page<BookEntity> findAllByPriceBetween(long min,long max, Pageable pageable);


}
