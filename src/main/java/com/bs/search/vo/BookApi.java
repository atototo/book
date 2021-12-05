package com.bs.search.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;
import java.util.List;


/**
 * packageName : com.bs.search.vo
 * fileName : BookApi
 * author : yelee
 * date : 2021-12-04
 * description :BookApi 조회 시 바로 매핑 되는 VO
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class BookApi implements Serializable {

    private static final long serialVersionUID = 8623971911951535024L;
    private Meta meta;
    private List<Documents> documents;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor      //json
    @Builder
    public static class Documents {
        private String url;
        private List<String> translators;
        private String title;
        private String thumbnail;
        private String status;
        private long salePrice;
        private String publisher;
        private long price;
        private String isbn;
        private String datetime;
        private String contents;
        private List<String> authors;

    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @ToString()
    public static class Meta {
        private int totalCount;
        private int pageableCount;
        private boolean isEnd;
    }
}
