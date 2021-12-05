package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * packageName : com.bs.search.domain
 * fileName : AuthorsEntityRepository
 * author : yelee
 * date : 2021-12-04
 * description : 도서별 저작권자 테이블 repository
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public interface AuthorsEntityRepository extends CrudRepository<AuthorsEntity, Long> {
}
