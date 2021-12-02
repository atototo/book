package com.bs.search.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "authors")
public class AuthorsEntity {

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성규칙
    @Column(name = "idx")
    private Long idx;

    @Column(name = "author_id")
    private Long id;

    @Column(name = "title")
    private String title;


    @Column(name = "author")
    private String author;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="book_id")
//    private BookEntity book;



}
