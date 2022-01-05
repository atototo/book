package com.bs.search.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


/**
 * packageName : com.bs.search.domain
 * fileName : AuthorsEntityRepository
 * author : yelee
 * date : 2021-12-04
 * description : 도서별 저작권자 테이블 repository
 * CrudRepository 로 상속받은 이유는 CRUD 기능 외에 사용 할 기능 없을거 같아서
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
public interface AuthorsEntityRepository extends JpaRepository<AuthorsEntity, Long> {
}
