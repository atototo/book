package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * packageName : com.bs.search.service
 * fileName : SearchApiService
 * author : isbn8
 * date : 2021-12-26
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-26       isbn8         최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearchApiService {

    private final RestTemplate restTemplate;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;


    /**
     * API 상태 확인용도
     * @param page
     * @param count
     * @return HttpStatus
     */
    public HttpStatus checkApiStatus(int page, int count) {
      return restTemplate.getForEntity(createUriComponents(page, count), BookApi.class).getStatusCode();
    }


    public ResponseEntity<BookApi> getBookApiResponseEntity() {
        return bookApiExcute(PageInfo.ChkCnt.REQ_CHK_PAGE_CNT.getCnt(), PageInfo.ChkCnt.REQ_MAX_CNT.getCnt());
    }

    public int getTotalCount(){
        return Objects.requireNonNull(bookApiExcute(PageInfo.ChkCnt.REQ_CHK_PAGE_CNT.getCnt(), PageInfo.ChkCnt.REQ_MAX_CNT.getCnt()).getBody())
                .getMeta()
                .getPageableCount();
    }


    public URI createUriComponents(int page, int count) {
        // UriComponents 는 URI 를 동적으로 생성해주는 클래스다.
        // 파라미터가 조합된 URI 를 손쉽게 만들어 주어 코드 상의 직접 문자열 조합 시의 실수를 방지 할 수 있다.
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page", page)
                .queryParam("size", count)
                .build();

        return uri.toUri();
    }




    public ResponseEntity<BookApi> bookApiExcute(int page, int count) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<?> request = new HttpEntity(headers);
        //HttpEntity 클래스 http 요청 또는 응답에 해당하는 HttpHeader, HttpBody 를 포함하고 있다.

        // 쿼리에 한글이 포함되기 때문에 인코딩 완료된  URI 타입으로 사용
        // exchange() http method any,  HTTP 헤더 만들 수 있다. 또한 ResponseEntity 형식으로 반환된다.
        return restTemplate.exchange(createUriComponents(page, count), HttpMethod.GET, request, BookApi.class);
    }

}
