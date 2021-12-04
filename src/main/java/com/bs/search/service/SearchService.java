package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.common.ErrorCode;
import com.bs.search.common.PageSearchEnum;
import com.bs.search.domain.*;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.exception.BooksNotFoundException;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final AuthorsEntityRepository authorsRepository;
    private final TranslatorsEntityRepository translatorsRepository;
    private final PagingRepository pagingRepository;


    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    private static long idxCnt = 1;
    private static int reqChkPageCnt = 5;
    private static int reqMaxCnt = 50;

    /**
     * 카카오 키워드 조회 결과 정보 전체 저장 용도
     */
    public void saveDocumentsAll() {

        //한번에 최대 50개씩

        var totalCnt = Objects.requireNonNull(createUriCompnentAndExcute(reqChkPageCnt, reqMaxCnt).getBody()).getMeta().getPageableCount();

        // vo형식 ->> 페이저블 참고
        int reqApiCnt = totalCnt%reqMaxCnt> 0? (totalCnt / reqMaxCnt )+1 : (totalCnt / reqMaxCnt );

        // API RES Documents 부 추출
        ArrayList<BookApi.Documents> listDoc = IntStream.rangeClosed(1, reqApiCnt)
                .mapToObj(i -> (ArrayList<BookApi.Documents>) Objects.requireNonNull(createUriCompnentAndExcute(i, reqMaxCnt).getBody()).getDocuments())
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        // API Documents BookEntity 관련 저장
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()-1).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i), i)).collect(Collectors.toCollection(ArrayList::new)));
        // Documents AuthoEntity 관련 저장
        authorsRepository.saveAll(makeDocumentsToAuthorsList(listDoc));
        // Documnets TranslaotrEntity 관련 저장
        translatorsRepository.saveAll(makeDocumentToTranslatorsList(listDoc));


    }

    /**
     * API 호출
     * @param page
     * @param count
     * @return
     */
    private ResponseEntity<BookApi> createUriCompnentAndExcute(int page, int count) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity request = new HttpEntity(headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page",  page)
                .queryParam("size", count)
                .build();

        return  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class);
    }

    /**
     * translator entity 추출
     * @param listDoc
     * @return
     */
    private List<TranslatorsEntity> makeDocumentToTranslatorsList(ArrayList<BookApi.Documents> listDoc) {
        ArrayList<TranslatorsEntity> listTranslators = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEach(i -> {
            List<String> listTrans = listDoc.get(i).getTranslators();
            if (!listTrans.isEmpty()) listTrans.stream()
                    .map(translator -> TranslatorsEntity.builder()
                            .trans_id((long) i)
                            .title(listDoc.get(i).getTitle())
                            .translator(translator)
                            .build())
                    .forEach(listTranslators::add);
        });
        return listTranslators;
    }

    /**
     * authors enttiy 추출
     * @param listDoc
     * @return
     */
    private List<AuthorsEntity> makeDocumentsToAuthorsList(ArrayList<BookApi.Documents> listDoc) {

        ArrayList<AuthorsEntity> listAuthors = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEachOrdered(i -> {
            List<String> listAuth = listDoc.get(i).getAuthors();
            if (!listAuth.isEmpty()) {
                listAuth.stream()
                        .map(author -> AuthorsEntity.builder()
                                .author_id((long) i)
                                .title(listDoc.get(i).getTitle())
                                .author(author).build()
                        )
                        .forEach(listAuthors::add);
            }
        });
        return listAuthors;
    }

    /**
     * 조회구분에따라 분기 하여 조회한다
     * @param pageSearchDto
     * @return
     */
    public ResponseEntity<Page<BookEntity>> findAllByTarget(PageSearchDto pageSearchDto) {
        ResponseEntity<Page<BookEntity>> response = null;
        if (PageSearchEnum.SEARCH_BY_TITLE.getValue().equals(pageSearchDto.getCdSearch())) {
            response = findAllByTitleLike(pageSearchDto);
        } else {
            response = findAllBooksByPrice(pageSearchDto);
        }
        return response;
    }

    /**
     * 도서 타이틀로 조회한다
     * @param pageSearchDto
     * @return  ResponseEntity<Page<BookEntity>>
     */
    @Cacheable(value = "product", key="#pageSearchDto.title")
    public  ResponseEntity<Page<BookEntity>> findAllByTitleLike(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), 10);

        Page<BookEntity> blist = pagingRepository.findByTitleContaining(pageSearchDto.getTitle(), page)
                .orElseThrow(() -> new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription()));
        return ResponseEntity.status(200).body(blist);
    }

    /**
     * 금액으로 조회한다
     * @param pageSearchDto
     * @return  ResponseEntity<Page<BookEntity>>
     */
    @Cacheable(value = "product", key= "#pageSearchDto.minPrice.toString().concat(:).concat(#PageSerchDto.maxPrice)")
    public  ResponseEntity<Page<BookEntity>> findAllBooksByPrice(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), 10);

        Page<BookEntity> blist = pagingRepository.findAllByPriceBetween(pageSearchDto.getMinPrice(), pageSearchDto.getMaxPrice(), page)
                .orElseThrow(() -> new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription()));
        return ResponseEntity.status(200).body(blist);
    }

}



