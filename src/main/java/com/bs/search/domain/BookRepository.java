package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * packageName : com.bs.search.domain
 * fileName : BookRepository
 * author : yelee
 * date : 2021-12-04
 * description : 도서 정보 repository
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Transactional
public interface BookRepository extends CrudRepository<BookEntity, Long> {

}
