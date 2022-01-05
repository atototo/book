package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.domain.*;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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


    //테스트를 다른곳에서 가져다 쓸 것이 아니기 때문에 그냥 Autowired 로 주입 받았다.
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorsEntityRepository authorsRepository;
    @Autowired
    private TranslatorsEntityRepository translatorsRepository;
    @Autowired
    private PagingBookRepository pagingBookRepository;


    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    private int totalCount;
    private UriComponents uri;
    private  HttpHeaders headers;

    @Test
    @DisplayName("API 요청이 잘 되는지 확인")
    void reqApi(){
        //given
        int page = 5;
        int count = 10;
        HttpEntity<?> request = new HttpEntity(headers);
        makeUrl(page, count);

        //when
        ResponseEntity<BookApi>  bookApiResponseEntity =  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class);

        //then
        assertAll(
                () -> Assertions.assertThat(bookApiResponseEntity.getStatusCodeValue()).hasToString("200"),
                () -> Assertions.assertThat(Objects.requireNonNull(bookApiResponseEntity.getBody()).getMeta().getTotalCount()).isPositive()
        );
    }



    @Test
    @DisplayName("API 통신 확인 테스트 ")
    void chkApiStauts() {
        //given
        int page =1;
        int count = 10;

        //when
        ResponseEntity<BookApi>  bookApiResponseEntity =  searchService.bookApiExcute(page, count);

        //then
        assertEquals(HttpStatus.OK, bookApiResponseEntity.getStatusCode());

    }

    @Test
    @DisplayName("API 통신 totalCount 에 따른 결과 확인")
    void getListDoc() {
        //given
        int totalCount = 100;

        //when
        List<BookApi.Documents> listDoc = searchService.listBookApiDoc(totalCount);

        //then
        assertEquals(totalCount, listDoc.size());
    }



    @BeforeEach
    void setHeaders() {
        headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
    }

    @Test
    @DisplayName("API 결과 데이터 수와 저장 된 데이터의 수가 일치 하는지 확인")
    void chkReqCnt() {

        //given
        PageInfo pageInfo = new PageInfo(342);
        int reqApiCnt = pageInfo.getReqApiCnt();
        HttpEntity<?> request = new HttpEntity(headers);


        // when
        ArrayList<BookApi.Documents> listDoc = new ArrayList<>();
        for (int i = 1; i <= reqApiCnt; i++) {
            makeUrl(i, 50);
            ArrayList<BookApi.Documents> documents = (ArrayList<BookApi.Documents>) Objects.requireNonNull(restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, BookApi.class)).getBody().getDocuments();
            listDoc.addAll(documents);
        }

        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i-1), i)).collect(Collectors.toCollection(ArrayList::new)));
        List<BookEntity> listBook = (List<BookEntity>) bookRepository.findAll();

        //then
       assertEquals(listDoc.size(), listBook.size());
    }


    @Test
    @DisplayName("카카오 키워드가 포함된 항목이 잘 조회 되었는지 확인")
    void checkTitleName() {
        //given
        makeUrl(1, 1);
        HttpEntity<?> request = new HttpEntity(headers);

        //when
        ResponseEntity<BookApi>  response =  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class);

        //then
        assertAll(
                () ->   assertNotNull(response.getBody()),
                () ->   assertTrue(Objects.requireNonNull(response.getBody()).getDocuments().get(0).getTitle().contains("카카오"))
        );

    }

    @Test
    @DisplayName("단건조회하여 같은 키값으로 도서,저자,번역 테이블에 각각 잘 저장 되는지확인")
    void saveDocumentsAll() {
        //given 단건만 조회되는 타이틀로 API 호출
        var title = "Go Go 카카오프렌즈. 1: 프랑스(윈터 에디션)";
        ResponseEntity<BookApi> response =  uriForSaveAll(title);
        LinkedList<BookApi.Documents> listDoc = IntStream.rangeClosed(1, 1).mapToObj(i1 -> (ArrayList<BookApi.Documents>) Objects.requireNonNull(response.getBody()).getDocuments())
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedList::new));

        //when : 각각 join 되어 있는 테이블에 저장
        // API Documents BookEntity 관련 저장
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i - 1), i)).collect(Collectors.toCollection(ArrayList::new)));
        // Documents AuthEntity 관련 저장
        authorsRepository.saveAll(Objects.requireNonNull(ReflectionTestUtils.invokeMethod(searchService, "makeDocumentsToAuthorsList", listDoc)));
        // Documents TranslatorEntity 관련 저장
        translatorsRepository.saveAll(Objects.requireNonNull(ReflectionTestUtils.invokeMethod(searchService, "makeDocumentToTranslatorsList", listDoc)));

        Pageable page = PageRequest.of(0, PageInfo.ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());
        Page<BookEntity> listBookEntity = pagingBookRepository.findByTitleContaining(title, page);

        //Then
        //1. 도서정보 테이블에 저장 확인
        assertEquals(title, listBookEntity.getContent().get(0).getTitle());
        //2. 동일한 ID 값으로 불러온 저자정보 도서명 일치 확인
        Optional<AuthorsEntity> listAuthors = authorsRepository.findById(1L);
        if (listAuthors.isPresent()) {
            System.out.println(listAuthors.toString());
            listAuthors.ifPresent(a -> {
                System.out.println(a.getAuthor());
                assertEquals(title, a.getTitle());
            });
        }
        //3. 동일한 ID 값으로 불러온 번역도서명 일치 확인
        Optional<TranslatorsEntity> listTranslators = translatorsRepository.findById(1L);
        if (listTranslators.isPresent()) {
            listTranslators.ifPresent(a -> {
                System.out.println(a.getTranslator());
                assertEquals(title, a.getTitle());
            });
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

    /**
     * 타이틀 별개로 호출용도 API 통신 메소드
     * @param title
     * @return
     */
    ResponseEntity<BookApi> uriForSaveAll(String title) {
        HttpEntity<?> request = new HttpEntity(headers);

        //uri 직접 지정 필요
        uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), title)
                .build();

        ResponseEntity<BookApi> response = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, BookApi.class);
        int cnt = Objects.requireNonNull(response.getBody()).getMeta().getTotalCount();

        return response;
    }

}