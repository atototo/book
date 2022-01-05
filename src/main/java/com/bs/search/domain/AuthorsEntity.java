package com.bs.search.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Parent;

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
@Entity     //JPA 관리 Entity 선언
@Table(name = "authors")
public class AuthorsEntity implements Serializable {

    //이렇게 직접 선언하지 않아도 내부에서 자동으로 생성하여 관리하지만
    //자동 관리 할 경우 생성 당시의 UID 와 현재 변경한 이후의 UID 가 맞지 않아서 문제가 발생 할 수 있기 때문에
    //JAVA 에서는 serialVersionUID 를 직접 선언하고 관리하는 방식을 적근 권장한다.
    private static final long serialVersionUID = 6789430517440297295L;

    @Id // 해당 테이블의 PK 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identity 전략 : DB가 아이디를 자동생성해주는 것
    @Column(name = "authorId") //@Column 별도의 아이디 지정시 지정한 name 으로 컬럼 생성된다
    private Long authorId;

//    @Column(name = "authorId")    //기존에 authorId 라고 했었는데 참조 키 이므로 bookId 가 더 적합하다 생각된다.
//    private Long authorId;

    @Column(name = "title")
    private String title;


    @Column(name = "author")
    private String author;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bookId")
    private BookEntity book;


}
