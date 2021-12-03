package com.bs.search.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class BookApi {

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("documents")
    private List<Documents> documents;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor      //json
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
