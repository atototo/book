package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.domain.*;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
        int totalCnt = createUriCompnentAndExcute(reqChkPageCnt, reqMaxCnt).getBody().getMeta().getPageableCount();
        int reqApiCnt = totalCnt%reqMaxCnt> 0? (totalCnt / reqMaxCnt )+1 : (totalCnt / reqMaxCnt );

        // API RES Documents 부 추출
        ArrayList<BookApi.Documents> listDoc = IntStream.rangeClosed(1, reqApiCnt).mapToObj(i -> (ArrayList<BookApi.Documents>) createUriCompnentAndExcute(i, reqMaxCnt).getBody().getDocuments()).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
        // API Documents BookEntity 관련 저장
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()-1).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiToEntity(listDoc.get(i), i)).collect(Collectors.toCollection(ArrayList::new)));
        // Documents AuthoEntity 관련 저장
        authorsRepository.saveAll(makeDocumenetsToAuthorsList(listDoc));
        // Documnets TranslaotrEntity 관련 저장
        translatorsRepository.saveAll(makeDocumenetsToTransotrsList(listDoc));
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

        return  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApi.class );
    }

    /**
     * translator entity 추출
     * @param listDoc
     * @return
     */
    private List<TranslatorsEntity> makeDocumenetsToTransotrsList( ArrayList<BookApi.Documents> listDoc) {
        ArrayList<TranslatorsEntity> listTranstors = new ArrayList<TranslatorsEntity>();
        for (int i = 1; i < listDoc.size(); i++) {
            List<String> listTrans = listDoc.get(i).getTranslators();
            if (0 < listTrans.size()) {
                for (String transtor : listTrans) {
                    listTranstors.add(TranslatorsEntity.builder()
                            .trans_id((long) i)
                            .title(listDoc.get(i).getTitle())
                            .translator(transtor)
                            .build()
                    );
                }
            }
        }
        return listTranstors;
    }

    /**
     * authors enttiy 추출
     * @param listDoc
     * @return
     */
    private List<AuthorsEntity> makeDocumenetsToAuthorsList( ArrayList<BookApi.Documents> listDoc) {

        ArrayList<AuthorsEntity> listAuthors = new ArrayList<AuthorsEntity>();
        for (int i = 1; i < listDoc.size(); i++) {
            List<String> listAuth = listDoc.get(i).getAuthors();
            if (0 < listAuth.size()) {
                for (String author : listAuth) {
                    listAuthors.add(AuthorsEntity.builder()
                            .author_id((long) i)
                            .title(listDoc.get(i).getTitle())
                            .author(author)
                            .build()
                    );
                }
            }
        }
        return listAuthors;
    }
    public List findAll() {
        return Collections.singletonList(bookRepository.findAll());
    }

    /**
     * 도서 타이틀로 조회
     * @param title
     * @return
     */
    public  ResponseEntity<List<?>> fildAllBooksByTitle(String title) {

        List<BookEntity> listBooks = bookRepository.findAllByTitle(title);

        if(listBooks.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(new String[]{"데이터 없음."}));
        }
        return ResponseEntity.status(200).body(listBooks);
    }

    /**
     * 정가금액 범위로 도서 검색
     * @param min
     * @param max
     * @return
     */
    public ResponseEntity<List<?>> fidAllBooksByPrice(long min, long max) {
        List<BookEntity> listBooks =  bookRepository.findAllByPriceBetween(min,max);

        if(listBooks.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(new String[]{"데이터 없음."}));
        }
        return ResponseEntity.status(200).body(listBooks);

    }

}



