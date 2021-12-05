package com.bs.search.domain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * packageName : com.bs.search.domain
 * fileName : TranslatorsEntity
 * author : yelee
 * date : 2021-12-04
 * description : 도서별 번역자 Entity
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
@Table(name = "translators")
public class TranslatorsEntity implements Serializable {

    private static final long serialVersionUID = 7773096788157725424L;

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성규칙
    @Column(name = "id")
    private Long id;

    @Column(name="trans_id")
    private Long transId;

    @Column(name = "title")
    private String title;

    @Column(name = "translators")
    private String translator;

}
