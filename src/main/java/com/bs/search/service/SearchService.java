package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.common.ErrorCode;
import com.bs.search.common.PageSearchEnum;
import com.bs.search.domain.*;
import com.bs.search.dto.BookInfoDto;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.exception.ApiNodataException;
import com.bs.search.exception.ApiUnstableException;
import com.bs.search.exception.BooksNotFoundException;
import com.bs.search.exception.RepositoryAccessException;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import com.bs.search.vo.PageInfo;
import com.bs.search.vo.PageInfo.ChkCnt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;


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
@RequiredArgsConstructor    //final이나 @NotNull을 사용한 필드에 대한 생성자를 자동으로 생성해준다
public class SearchService {
    // 생성자 주입 방법으로 의존성 주입 권장한다.
    // 1. 순환 참조 방지
    // 2. final 선언이 가능
    // 3. 테스트 코드 작성 용이
    //
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final AuthorsEntityRepository authorsRepository;
    private final TranslatorsEntityRepository translatorsRepository;
    private final PagingBookRepository pagingRepository;
    private final SearchApiService searchApiService;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    /**
     * methodName : searchHome
     * author : yelee
     * description : API 카카오 키워드 조회하여 모두 저장
     * @void
     */
    @Transactional(rollbackOn = Exception.class)
    public void saveDocumentsAllNew() {

        //한번에 최대 50개씩 :: API 최대 제공 개수로 지정 위해서
        ResponseEntity<BookApi> responseEntity;
        try {
            responseEntity = requireNonNull(searchApiService.bookApiExcute(ChkCnt.REQ_CHK_PAGE_CNT.getCnt(), ChkCnt.REQ_MAX_CNT.getCnt()));

        } catch (RestClientException e) {
            log.info("RestClientException");
            throw new ApiUnstableException();

        }

        // API 조회에는 성공했으나 조회 건수가 없는 경우 개발자에게 알림 후 종료
        int totalCnt = requireNonNull(responseEntity.getBody()).getMeta().getTotalCount();
        if (totalCnt == 0) {
            throw new ApiNodataException("API 조회 건수가 없습니다.");
        }

        // API RES Documents 부분 추출
        LinkedList<BookApi.Documents> listDoc = (LinkedList<BookApi.Documents>) listBookApiDoc(totalCnt);
        // API Documents BookEntity 관련 저장
        // id 넣어줄 때 0부터 넣을 수 는 없어서 1로 시작
        bookSaveAll(listDoc);

        // Documents AuthEntity 관련 저장
        authorsSaveAll(listDoc);

        // Documents TranslatorEntity 관련 저장
        translatorsSaveAll(listDoc);

    }

    /**
     * API RES Documents 부분 추출
     * @param totalCnt
     * @return List<BookApi.Documents>
     */
    public List<BookApi.Documents> listBookApiDoc(int totalCnt){
        PageInfo pageInfo = new PageInfo(totalCnt);
        int reqApiCnt = pageInfo.getReqApiCnt();
        // API RES Documents 부분 추출

        return IntStream.rangeClosed(1, reqApiCnt)
                .mapToObj(i -> (ArrayList<BookApi.Documents>) requireNonNull(searchApiService.bookApiExcute(i, ChkCnt.REQ_MAX_CNT.getCnt()).getBody()).getDocuments())
                .flatMap(Collection::stream)
                //위에서 받은 배열을 하나의 stream 반환 한다는 의미
                .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     * API Document book 정보 저장
     * @param listDoc
     */
    public void bookSaveAll(List<BookApi.Documents> listDoc) {

        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size())
                .mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i - 1), i))
                .collect(Collectors.toCollection(ArrayList::new)));

    }

    /**
     * API Book authors 정보 저장
     * @param listDoc
     */
    public void authorsSaveAll(List<BookApi.Documents> listDoc) {
        authorsRepository.saveAll(makeDocumentsToAuthorsList((LinkedList<BookApi.Documents>) listDoc));
    }


    /**
     * API Book translators 정보 저장
     * @param listDoc
     */
    public void translatorsSaveAll(List<BookApi.Documents> listDoc) {
        translatorsRepository.saveAll(makeDocumentToTranslatorsList((LinkedList<BookApi.Documents>) listDoc));
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
            List<String> listTrans = listDoc.get(i - 1).getTranslators();        // 책 한권당 저작궈자 여러명이고 없을 수도 있다.
            BookEntity book = DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i - 1), i);
            if (listTrans.isEmpty()) {
                return;
            }
            listTrans.stream().map(translator -> TranslatorsEntity.builder()
                            .title(book.getTitle())
                            .translator(translator)
                            .build())
                    .forEach(translatorsEntity -> {
                        translatorsEntity.setBook(book);
                        listTranslators.add(translatorsEntity);
                    });

        });
        return listTranslators;
    }

    /**
     * methodName : makeDocumentsToAuthorsList
     * author : yelee
     * description : API response documents 부분 중 author 추출
     * mapper 를 사용하기엔 형식 과 검증이 맞지 않아 builder  사용이 효율적으로 생각되어서 이렇게 구현
     * @param listDoc
     * @return List<AuthorsEntity>
     */
    private List<AuthorsEntity> makeDocumentsToAuthorsList(LinkedList<BookApi.Documents> listDoc) {

        ArrayList<AuthorsEntity> listAuthors = new ArrayList<>();

        IntStream.range(1, listDoc.size()).forEachOrdered(i -> {
            List<String> listAuth = listDoc.get(i - 1).getAuthors();        // 책 한권당 저작궈자 여러명이고 없을 수도 있다.

            BookEntity book = DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i - 1), i);
            if (listAuth.isEmpty()) {
                return;
            }
            listAuth.stream().map(author -> AuthorsEntity.builder()
                    .title(book.getTitle())
                    .author(author)
                    .build()).forEach(authorsEntity -> {
                authorsEntity.setBook(book);
                listAuthors.add(authorsEntity);
            });

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
        Page<BookEntity> bookInfoDtoPage = null;
        if (PageSearchEnum.SEARCH_BY_TITLE.getValue().equals(pageSearchDto.getCdSearch())) {
            bookInfoDtoPage = findAllByTitleLike(pageSearchDto);
        } else {
            bookInfoDtoPage = findAllBooksByPrice(pageSearchDto);
        }
        return bookInfoDtoPage;
    }

    /**
     * methodName : findAllByTitleLike
     * author : yelee
     * description : 조회구분 타이틀의 경우 호출되는 메소드
     *
     * @param  pageSearchDto
     * @return Page<BookEntity>
     */
    public Page<BookEntity> findAllByTitleLike(PageSearchDto pageSearchDto) {
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> listBook = pagingRepository.findByTitleContaining(pageSearchDto.getTitle(), page);

        if (!listBook.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }

        return listBook;

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
        Pageable page = PageRequest.of(pageSearchDto.getPageNum(), ChkCnt.REQ_DEFAULT_PAGE_SIZE.getCnt());

        Page<BookEntity> listBook = pagingRepository.findAllByPriceBetween(pageSearchDto.getMinPrice(), pageSearchDto.getMaxPrice(), page);

        if (!listBook.hasContent()) {
            throw new BooksNotFoundException(ErrorCode.NOT_FOND.getDescription());
        }
        return listBook;
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
        return new StringJoiner("_")
                .add(pageSearchDto.getTitle())
                .add(String.valueOf(pageSearchDto.getPageNum()))
                .add(String.valueOf(pageSearchDto.getMinPrice()))
                .add(String.valueOf(pageSearchDto.getMaxPrice()))
                .toString();

    }


}



