package com.bs.search.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * packageName : com.bs.search.dto
 * fileName : BookInfoDto
 * author : isbn8
 * date : 2021-12-25
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-25       isbn8         최초 생성
 */
@Getter
@Setter
@ToString
@Builder
public class BookInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private long price;


    public BookInfoDto(Long id, String title, long price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
}
