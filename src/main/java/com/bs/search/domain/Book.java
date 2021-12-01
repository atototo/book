package com.bs.search.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "book")
public class Book {

    @Id // 해당 테이블의 PK 필드
    @Column(name = "id")
    private Long id;

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
    private int price;

    @Column(name = "salePrice")
    private int salePrice;

    @Column(name = "thumbnail" , length = 1000)
    private String thumbnail;

    @Column(name = "status")
    private String status;

    public Optional<String> getStatusNull() {
        return Optional.ofNullable(status);
    }
}