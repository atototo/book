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

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;


    /**
     * methodName : searchHome
     * author : yelee
     * description : API 카카오 키워드 조회하여 모두 저장
     *
     * @void
     */
    public void saveDocumentsAll() {

        //한번에 최대 50개씩
        var totalCnt = Objects.requireNonNull(createUriCompnentAndExcute(PageInfo.ChkCnt.REQ_CHK_PAGE_CNT.getCnt(), PageInfo.ChkCnt.REQ_MAX_CNT.getCnt()).getBody()).getMeta().getPageableCount();

        PageInfo pageInfo = new PageInfo(totalCnt);
        int reqApiCnt = pageInfo.getReqApiCnt();

        // API RES Documents 부분 추출
        LinkedList<BookApi.Documents> listDoc = IntStream.rangeClosed(1, reqApiCnt)
                .mapToObj(i -> (ArrayList<BookApi.Documents>) Objects.requireNonNull(createUriCompnentAndExcute(i, PageInfo.ChkCnt.REQ_MAX_CNT.getCnt()).getBody()).getDocuments())
                .flatMap(Collection::stream).collect(Collectors.toCollection(LinkedList::new));
        // API Documents BookEntity 관련 저장
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i - 1), i)).collect(Collectors.toCollection(ArrayList::new)));
        // Documents AuthEntity 관련 저장
        authorsRepository.saveAll(makeDocumentsToAuthorsList(listDoc));
        // Documents TranslatorEntity 관련 저장
        translatorsRepository.saveAll(makeDocumentToTranslatorsList(listDoc));


    }

    /**
     * methodName : createUriCompnentAndExcute
     * author : yelee
     * description : API 요청 메소드
     *
     * @param page
     * @param count
     * @return ResponseEntity<BookApi>
     */
    private ResponseEntity<BookApi> createUriCompnentAndExcute(int page, int count) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity<?> request = new HttpEntity(headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page", page)
                .queryParam("size", count)
                .build();

        return restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, BookApi.class);
    }

    /**
     * methodName : makeDocumentToTranslatorsList
     * author : yelee
     * description :API response documents 부분 중 translator 추출
     *
     * @param listDoc
     * @return List<TranslatorsEntity
     */
    private List<TranslatorsEntity> makeDocumentToTranslatorsList(LinkedList<BookApi.Documents> listDoc) {
        ArrayList<TranslatorsEntity> listTranslators = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEachOrdered(i -> {
            List<String> listTrans = listDoc.get(i - 1).getTranslators();
            if (!listTrans.isEmpty()) listTrans.stream()
                    .map(translator -> TranslatorsEntity.builder()
                            .transId((long) i)
                            .title(listDoc.get(i - 1).getTitle())
                            .translator(translator)
                            .build())
                    .forEach(listTranslators::add);
        });
        return listTranslators;
    }

    /**
     * methodName : makeDocumentsToAuthorsList
     * author : yelee
     * description : API response documents 부분 중 author 추출
     *
     * @param listDoc
     * @return List<AuthorsEntity>
     */
    private List<AuthorsEntity> makeDocumentsToAuthorsList(LinkedList<BookApi.Documents> listDoc) {

        ArrayList<AuthorsEntity> listAuthors = new ArrayList<>();
        IntStream.range(1, listDoc.size()).forEachOrdered(i -> {
            List<String> listAuth = listDoc.get(i - 1).getAuthors();
            if (!listAuth.isEmpty()) {
                listAuth.stream()
                        .map(author -> AuthorsEntity.builder()
                                .authorId((long) i)
                                .title(listDoc.get(i - 1).getTitle())
                                .author(author).build()
                        )
                        .forEach(listAuthors::add);
            }
        });
        return listAuthors;
    }

    /**
     * /**
     * methodName : findAllByTarget
     * author : yelee
     * description : 조회구분에따라 필요 메소드 호출 메소드로 캐싱 처리
     *
     * @param pageSearchDto
     * @return Page<BookEntity>
     */
    @Cacheable(value = "search", key = "T(com.bs.search.service.SearchService).makeKey(#pageSearchDto)")
    public Page<BookEntity> findAllByTarget(PageSearchDto pageSearchDto) {
        Page<BookEntity> response = null;
        if (PageSearchEnum.SEARCH_BY_TITLE.getValue().equals(pageSearchDto.getCdSearch())) {
            response = findAllByTitleLike(pageSearchDto);
        } else {
            response = findAllBooksByPrice(pageSearchDto);
        }
        return response;
    }

    /**
     * methodName : findAllByTitleLike
     * author : yelee
     * description : 조회구분 타이틀의 경우 호출되는 메소드
     *
     * @param pageSearchDto
     * @return Page<BookEntity>
     */
    public Page<BookEntity> findAllByTitleLike(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), PageInfo.ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> response = pagingRepository.findByTitleContaining(pageSearchDto.getTitle(), page);

        if (!response.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }
        return response;
    }

    /**
     * methodName : findAllBooksByPrice
     * author : yelee
     * description : 조회구분 가격범위의 경우 호출되는 메소드
     *
     * @param pageSearchDto
     * @return response
     */
    public Page<BookEntity> findAllBooksByPrice(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), PageInfo.ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> response = pagingRepository.findAllByPriceBetween(pageSearchDto.getMinPrice(), pageSearchDto.getMaxPrice(), page);

        if (!response.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }
        return response;
    }

    /**
     * methodName : makeKey
     * author : yelee
     * description : 캐시 키 생성 메소드 (title_pageNum_minPrice_maxPrice)
     *
     * @param pageSearchDto
     * @return string
     */
    public static String makeKey(PageSearchDto pageSearchDto) {
        return pageSearchDto.getTitle() + "_" + pageSearchDto.getPageNum() + "_" + pageSearchDto.getMinPrice() + "_" + pageSearchDto.getMaxPrice();
    }

}



