package com.bs.search.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class BookApi {

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
