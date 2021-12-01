package com.bs.search.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Documents {
    private String title;
    private String contents;
    private String url;
    private String isbn;
    private String datetime;
    private List<Authors> authors;
    private String publisher;
    private List<Translators> translators;
    private int price;
    private int salePrice;
    private String thumbnail;
    private String status;
}
