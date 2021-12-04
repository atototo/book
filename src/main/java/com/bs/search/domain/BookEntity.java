package com.bs.search.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

/**
 * packageName : com.bs.search.domain
 * fileName : AuthorsEntity
 * author : yelee
 * date : 2021-12-04
 * description : 도서 정보 Entity
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
@Table(name = "book")
public class BookEntity implements Serializable {

    private static final long serialVersionUID = -6664369969831285247L;
    @Id // 해당 테이블의 PK 필드
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Column(name = "title")
    private String title;

    @Column(name = "contents", length = 2000)
    private String contents;

    @Column(name = "url" , length = 1000)
    private String url;

    @Column(name = "isbn" , length = 500)
    private String isbn;

    @Column(name = "datetime")
    private String datetime;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "price")
    private long price;

    @Column(name = "salePrice")
    private long salePrice;

    @Column(name = "thumbnail" , length = 1000)
    private String thumbnail;

    @Column(name = "status")
    private String status;

    @OneToMany( fetch = FetchType.LAZY)
    @JoinColumn(name="author_id")
    private Set<AuthorsEntity> setAuthorEntitys;

    @OneToMany( fetch = FetchType.LAZY)
    @JoinColumn(name="trans_id")
    private Set<TranslatorsEntity> translatorAuthorEntitys;

}
