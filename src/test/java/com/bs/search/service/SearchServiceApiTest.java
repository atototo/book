package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.domain.BookEntity;
import com.bs.search.domain.BookRepository;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Autowired
    private BookRepository bookRepository;


    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    private int totalCount;
    private UriComponents uri;

    @Test
    @DisplayName("API 요청이 잘 되는지 확인")
    void reqApi(){
        //given
        int page = 5;
        int count = 10;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<?> request = new HttpEntity(headers);
        makeUrl(page, count);

        //when
        ResponseEntity<BookApi>  response =  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class);

        //then
        assertAll(
                () -> Assertions.assertThat(response.getStatusCodeValue()).hasToString("200"),
                () -> Assertions.assertThat(Objects.requireNonNull(response.getBody()).getMeta().getTotalCount()).isPositive()
        );

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            totalCount = response.getBody().getMeta().getTotalCount();
            System.out.println("totalCount :  " + totalCount);
        }

    }

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

    @Test
    @DisplayName("Api요청 결과의 데이터 수와 저장 된 데이터의 수가 일치 하는지 확인")
    void chkReqCnt() {

        //given
        PageInfo pageInfo = new PageInfo(342);
        int reqApiCnt = pageInfo.getReqApiCnt();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<?> request = new HttpEntity(headers);


        // when
        ArrayList<BookApi.Documents> listDoc = new ArrayList<>();
        for (int i = 1; i <= reqApiCnt; i++) {
            makeUrl(i, 50);
            ArrayList<BookApi.Documents> documents = (ArrayList<BookApi.Documents>) Objects.requireNonNull(restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, BookApi.class)).getBody().getDocuments();
            for (BookApi.Documents document : documents) {
                listDoc.add(document);
            }
        }

        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i-1), i)).collect(Collectors.toCollection(ArrayList::new)));
        List<BookEntity> listBook = (List<BookEntity>) bookRepository.findAll();

        //then
       assertEquals(listDoc.size(), listBook.size());
    }



}