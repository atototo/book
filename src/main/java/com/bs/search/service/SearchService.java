package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.common.ErrorCode;
import com.bs.search.common.PageSearchEnum;
import com.bs.search.domain.*;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.exception.BooksNotFoundException;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * packageName : com.bs.search.service
 * fileName : SearchService
 * author : yelee
 * date : 2021-12-04
 * description : Application service 로직
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final AuthorsEntityRepository authorsRepository;
    private final TranslatorsEntityRepository translatorsRepository;
    private final PagingBookRepository pagingRepository;
    private final CacheManager cacheManager;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    /**
     * 카카오 키워드 조회 결과 정보 전체 저장 용도
     */
    public void saveDocumentsAll() {

        //한번에 최대 50개씩
        var totalCnt = Objects.requireNonNull(createUriCompnentAndExcute(PageInfo.ChkCnt.REQ_CHK_PAGE_CNT.getCnt(), PageInfo.ChkCnt.REQ_MAX_CNT.getCnt()).getBody()).getMeta().getPageableCount();

        PageInfo pageInfo = new PageInfo(totalCnt);
        int reqApiCnt = pageInfo.getReqApiCnt();

        // API RES Documents 부 추출
        ArrayList<BookApi.Documents> listDoc = IntStream.rangeClosed(1, reqApiCnt)
                .mapToObj(i -> (ArrayList<BookApi.Documents>) Objects.requireNonNull(createUriCompnentAndExcute(i,  PageInfo.ChkCnt.REQ_MAX_CNT.getCnt()).getBody()).getDocuments())
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        // API Documents BookEntity 관련 저장
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()-1).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i), i)).collect(Collectors.toCollection(ArrayList::new)));
        // Documents AuthEntity 관련 저장
        authorsRepository.saveAll(makeDocumentsToAuthorsList(listDoc));
        // Documents TranslatorEntity 관련 저장
        translatorsRepository.saveAll(makeDocumentToTranslatorsList(listDoc));


    }

    /**
     * API 호출
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
     */
    private List<TranslatorsEntity> makeDocumentToTranslatorsList(ArrayList<BookApi.Documents> listDoc) {
        ArrayList<TranslatorsEntity> listTranslators = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEach(i -> {
            List<String> listTrans = listDoc.get(i).getTranslators();
            if (!listTrans.isEmpty()) listTrans.stream()
                    .map(translator -> TranslatorsEntity.builder()
                            .transId((long) i)
                            .title(listDoc.get(i).getTitle())
                            .translator(translator)
                            .build())
                    .forEach(listTranslators::add);
        });
        return listTranslators;
    }

    /**
     * authors entity 추출
     * @return
     */
    private List<AuthorsEntity> makeDocumentsToAuthorsList(ArrayList<BookApi.Documents> listDoc) {

        ArrayList<AuthorsEntity> listAuthors = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEachOrdered(i -> {
            List<String> listAuth = listDoc.get(i).getAuthors();
            if (!listAuth.isEmpty()) {
                listAuth.stream()
                        .map(author -> AuthorsEntity.builder()
                                .authorId((long) i)
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
    @Cacheable(value = "search", key = "T(com.bs.search.service.SearchService).generate(#pageSearchDto)")
    public Page<BookEntity> findAllByTarget(PageSearchDto pageSearchDto) {
        Page<BookEntity> response = null;
        if (PageSearchEnum.SEARCH_BY_TITLE.getValue().equals(pageSearchDto.getCdSearch())) {
            response = findAllByTitleLike(pageSearchDto);
        } else {
            response = findAllBooksByPrice(pageSearchDto);
        }
         log.info("cache :: {}" ,  cacheManager.getCache("search").getNativeCache().toString());
        return response;
    }

    /**
     * 도서 타이틀로 조회한다
     * @return  ResponseEntity<Page<BookEntity>>
     */
    public  Page<BookEntity> findAllByTitleLike(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(),  PageInfo.ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> response = pagingRepository.findByTitleContaining(pageSearchDto.getTitle(), page);

        if (!response.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }
        return response;
    }

    /**
     * 금액으로 조회한다.
     * @return  ResponseEntity<Page<BookEntity>>
     */
    public Page<BookEntity> findAllBooksByPrice(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), PageInfo.ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> response = pagingRepository.findAllByPriceBetween(pageSearchDto.getMinPrice(), pageSearchDto.getMaxPrice(), page);

        if (!response.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }
        return response;
    }

    public static Object generate(PageSearchDto pageSearchDto) {
        return pageSearchDto.getTitle()+"_"+pageSearchDto.getPageNum() + "_" + pageSearchDto.getMinPrice()+"_"+ pageSearchDto.getMaxPrice();
    }

}



