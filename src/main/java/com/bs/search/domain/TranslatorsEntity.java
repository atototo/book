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
@Table(name = "translators")
public class TranslatorsEntity {

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK생성규칙
    @Column(name = "idx")
    private Long idx;

    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id")
    private BookEntity book;

    @Column(name = "title")
    private String title;


    @Column(name = "translators")
    private String translator;

}
