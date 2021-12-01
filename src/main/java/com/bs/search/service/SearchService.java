package com.bs.search.service;

import com.bs.search.common.ApiEnum;
import com.bs.search.domain.*;
import com.bs.search.dto.DocumentsDto;
import com.bs.search.mapper.DocumentsMapper;
import com.bs.search.vo.BookApiVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final AuthorsRepository authorsRepository;
    private final TranslatorsRepository translatorsRepository;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;

    private static long idxCnt = 1;
    private static int reqChkPageCnt = 5;
    private static int reqMaxCnt = 50;

    private ResponseEntity<BookApiVO> createUriCompnentAndExcute(int page, int count) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, key);
        HttpEntity request = new HttpEntity(headers);
        //메소드 분리
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam(ApiEnum.TARGET_KEY.getValue(), ApiEnum.TARGET_VALUE.getValue())
                .queryParam(ApiEnum.QUERY_KEY.getValue(), ApiEnum.QUERY_VALUE.getValue())
                .queryParam("page",  page)
                .queryParam("size", count)
                .build();

      return  restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApiVO.class );
    }

    /**
     * 카카오 키워드 조회 결과 정보 전체 저장 용도
     */
    public void saveDocumentsAll() {
        //한번에 최대 50개씩
        int totalCnt = createUriCompnentAndExcute(reqChkPageCnt, reqMaxCnt).getBody().getMeta().getPageableCount();
        int reqApiCnt = totalCnt%reqMaxCnt> 0? (totalCnt / reqMaxCnt )+1 : (totalCnt / reqMaxCnt );

        ArrayList<BookApiVO.Documents> listDoc = IntStream.rangeClosed(1, reqApiCnt).mapToObj(i -> (ArrayList<BookApiVO.Documents>) createUriCompnentAndExcute(i, reqMaxCnt).getBody().getDocuments()).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
        bookRepository.saveAll(IntStream.rangeClosed(1, listDoc.size()-1).mapToObj(i -> DocumentsMapper.INSTANCE.bookApiVOToEntity(listDoc.get(i), i)).collect(Collectors.toCollection(ArrayList::new)));
        authorsRepository.saveAll(makeDocumenetsToAuthorsList(listDoc));
        translatorsRepository.saveAll(makeDocumenetsToTransotrsList(listDoc));
    }

    /**
     * translator entity 추출
     * @param listDoc
     * @return
     */
    private List<TranslatorsEntity> makeDocumenetsToTransotrsList( ArrayList<BookApiVO.Documents> listDoc ) {
        ArrayList<TranslatorsEntity> listTranstors = new ArrayList<TranslatorsEntity>();
        for (int i = 1; i < listDoc.size(); i++) {
            List<String> listTrans = listDoc.get(i).getTranslators();
            if (0 < listTrans.size()) {
                for (String transtor : listTrans) {
                    listTranstors.add(TranslatorsEntity.builder()
                            .id((long) i)
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
    private List<AuthorsEntity> makeDocumenetsToAuthorsList( ArrayList<BookApiVO.Documents> listDoc ) {
        ArrayList<AuthorsEntity> listAuthors = new ArrayList<AuthorsEntity>();
        for (int i = 1; i < listDoc.size(); i++) {
            List<String> listAuth = listDoc.get(i).getAuthors();
            if (0 < listAuth.size()) {
                for (String author : listAuth) {
                    listAuthors.add(AuthorsEntity.builder()
                            .id((long) i)
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

    public List<BookEntity> fildAllBooksByTitle(String title) {
        return bookRepository.findAllByTitle(title);
    }

    public List<BookEntity> fidAllBooksByPrice(long min, long max) {
       return bookRepository.findAllByPriceBetween(min,max);
    }

    private List<AuthorsEntity> findAllAuthorsByTitle(String title) {
        return authorsRepository.findAllByTitle(title);
    }

    private List<TranslatorsEntity> findAllTranslatorsByTitle(String title) {
        return translatorsRepository.findAllByTitle(title);
    }

    public List<DocumentsDto> findAllDocumentsByTitle(String title) {

        List<BookEntity> listBook = fildAllBooksByTitle(title);
        Optional<List<BookEntity>> listOptBook = Optional.of(listBook);

        List<AuthorsEntity> listAuthors = findAllAuthorsByTitle(title);
        Optional<List<AuthorsEntity>> listOptAuth = Optional.of(listAuthors);

        List<TranslatorsEntity> listTranslator = findAllTranslatorsByTitle(title);
        Optional<List<TranslatorsEntity>> listOptTrans = Optional.of(listTranslator);

        List<DocumentsDto> listDoc = new ArrayList<>();
        if (!listOptBook.isEmpty()) {
            listBook.parallelStream().forEach(s->{

                List<AuthorsEntity> listAuth = new ArrayList<>( );
                if(!listOptAuth.isEmpty()) {
                    listAuth = listAuthors.parallelStream().filter(y -> y.getId().equals(s.getId())).collect(Collectors.toList());
                }

                List<TranslatorsEntity> listTrans = new ArrayList<>( );
                if (!listOptTrans.isEmpty()) {
                    listTrans = listTranslator.parallelStream().filter(y -> y.getId().equals(s.getId())).collect(Collectors.toList());

                }



                listDoc.add(DocumentsDto.builder()
                        .title(s.getTitle())
                        .authors(listAuth)
                        .contents(s.getContents())
                        .thumbnail(s.getThumbnail())
                        .isbn(s.getIsbn())
                        .publisher(s.getPublisher())
                        .datetime(s.getDatetime())
                        .price(s.getPrice())
                        .salePrice(s.getSalePrice())
                        .status(s.getStatus())
                        .url(s.getUrl())
                        .translators(listTrans)
                        .build()
                );
            });
        }

        return listDoc;
    }
}
