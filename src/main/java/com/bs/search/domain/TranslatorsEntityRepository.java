package com.bs.search.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * packageName : com.bs.search.domain
 * fileName : TranslatorsEntityRepository
 * author : yelee
 * date : 2021-12-04
 * description : 도서별 번역자 Repository
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public interface TranslatorsEntityRepository extends CrudRepository<TranslatorsEntity, Long> {


}
