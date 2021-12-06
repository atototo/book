package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.domain.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : com.bs.search.service
 * fileName : SearchServiceTest
 * author : isbn8
 * date : 2021-12-06
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-06       isbn8         최초 생성
 */
class SearchServiceTest {
    @Autowired
    SearchService searchService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;


    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    private int totalCount;
    private UriComponents uri;
    private HttpHeaders headers;

    /**
     * 요청 Uri 생성 용도
     * @param page
     * @param count
     */
    void makeUrl(int page, int count) {

        uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page",  page)
                .queryParam("size", count)
                .build();

    }

    @BeforeEach
    void setHeaders() {
        headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveDocumentsAll() {
    }

    @Test
    void findAllByTarget() {
    }

    @Test
    void findAllByTitleLike() {
    }

    @Test
    void findAllBooksByPrice() {
    }

    @Test
    void makeKey() {
    }
}