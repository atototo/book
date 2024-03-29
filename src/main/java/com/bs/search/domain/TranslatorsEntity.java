package com.bs.search.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transId")
    private Long transId;

//    @Column(name = "transId") //기존에 transId 라고 했었는데 참조 키 이므로 bookId 가 더 적합하다 생각된다.
//    private Long transId;

    @Column(name = "title")
    private String title;

    @Column(name = "translators")
    private String translator;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity book;


}
