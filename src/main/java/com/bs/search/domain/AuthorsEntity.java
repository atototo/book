package com.bs.search.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


/**
 * packageName : com.bs.search.domain
 * fileName : AuthorsEntity
 * author : yelee
 * date : 2021-12-04
 * description : 도서별 저작권자 Entity
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authors")
public class AuthorsEntity implements Serializable {

    private static final long serialVersionUID = 6789430517440297295L;

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "title")
    private String title;


    @Column(name = "author")
    private String author;

}
