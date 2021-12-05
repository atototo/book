package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : com.bs.search.service
 * fileName : SearchServiceApiTest
 * author : isbn8
 * date : 2021-12-06
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-06       isbn8         최초 생성
 */
@Slf4j
@SpringBootTest
class SearchServiceApiTest {
    @Autowired
    SearchService searchService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    @Test
    @DisplayName("API 요청이 잘 되는지 확인")
    void reqApi(){
        //given
        int page = 5;
        int count = 10;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<?> request = new HttpEntity(headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page",  page)
                .queryParam("size", count)
                .build();

        //when
        ResponseEntity<BookApi>  response =  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class);

        //then
        assertAll(
                () -> Assertions.assertThat(response.getStatusCodeValue()).hasToString("200"),
                () -> Assertions.assertThat(Objects.requireNonNull(response.getBody()).getMeta().getTotalCount()).isPositive()
        );

    }


}