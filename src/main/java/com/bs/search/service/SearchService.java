package com.bs.search.service;

import com.bs.search.domain.*;
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

    public void saveAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", key);
        HttpEntity request = new HttpEntity(headers);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                .queryParam("target", "title")
                .queryParam("query", "카카오")
                .build();

        //처음 개수 확인 용도 req
        ResponseEntity<BookApiVO> response = restTemplate.exchange( uri.toUri(), HttpMethod.GET, request, BookApiVO.class );

        //한번에 최대 50개씩
        int totalCnt = response.getBody().getMeta().getTotalCount();

        HashMap<Long,ArrayList<BookApiVO.Documents>> docMap = new HashMap<Long,ArrayList<BookApiVO.Documents>>();
        long reqCnt = 1;
        while (totalCnt > 0) {
            UriComponents  reUri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                    .queryParam("target", "title")
                    .queryParam("query", "카카오")
                    .queryParam("page",  reqCnt)
                    .queryParam("size", 50)
                    .build();
            ResponseEntity<BookApiVO>   resApi = restTemplate.exchange( reUri.toUri(), HttpMethod.GET, request, BookApiVO.class );
            ArrayList<BookApiVO.Documents>  listDoc = (ArrayList<BookApiVO.Documents>) resApi.getBody().getDocuments();

            docMap.put(reqCnt,listDoc);
            reqCnt+=1;
            totalCnt = totalCnt - listDoc.size();
        }

        ArrayList<Book> listBook = new ArrayList<Book>();
        ArrayList<Translators> listTrans = new ArrayList<Translators>();
        ArrayList<Authors> listAuthors = new ArrayList<Authors>();
        Optional<BookApiVO.Documents> mayDoc = Optional.empty();
        docMap.forEach((k,v) -> {
            v.stream().forEach(s -> {

                listBook.add(Book.builder()
                        .title(s.getTitle())
                        .contents(s.getContents())
                        .url(s.getUrl())
                        .isbn(s.getUrl())
                        .datetime(s.getDatetime())
                        .price(s.getPrice())
                        .salePrice(s.getSalePrice())
                        .thumbnail(s.getThumbnail())
                        .status(s.getStatus())
                        .id(idxCnt)
                        .build()
                );


                if(s.getAuthors().size() > 0) {
                    listAuthors.add(Authors.builder()
                            .id(idxCnt)
                            .title(s.getTitle())
                            .author(s.getAuthors().stream().collect(Collectors.joining(" ,")))
                            .build()
                    );
                };

                if (s.getTranslators().size() > 0) {
                    listTrans.add(Translators.builder()
                            .id(idxCnt)
                            .title(s.getTitle())
                            .translator(s.getTranslators().stream().collect(Collectors.joining(" ,")))
                            .build()
                    );
                }
                idxCnt++;
            });
        });

        bookRepository.saveAll(listBook);
        authorsRepository.saveAll(listAuthors);
        translatorsRepository.saveAll(listTrans);
    }


    public List findAll() {
        return Collections.singletonList(bookRepository.findAll());
    }

    public List<Book> fildAllBooksByTitle(String title) {
        return bookRepository.findAllByTitle(title);
    }

    public List<Book> fidAllBooksByPrice(long min, long max) {
       return bookRepository.findAllByPriceBetween(min,max);
    }

    private List<Authors> findAllAuthorsByTitle(String title) {
        return authorsRepository.findAllByTitle(title);
    }

    private List<Translators> findAllTranslatorsByTitle(String title) {
        return translatorsRepository.findAllByTitle(title);
    }

    public List<Documents> findAllDocumentsByTitle(String title) {

        List<Book> listBook = fildAllBooksByTitle(title);
        Optional<List<Book>> listOptBook = Optional.of(listBook);

        List<Authors> listAuthors = findAllAuthorsByTitle(title);
        Optional<List<Authors>> listOptAuth = Optional.of(listAuthors);

        List<Translators> listTranslator = findAllTranslatorsByTitle(title);
        Optional<List<Translators>> listOptTrans = Optional.of(listTranslator);

        List<Documents> listDoc = new ArrayList<>();
        if (!listOptBook.isEmpty()) {
            listBook.parallelStream().forEach(s->{

                List<Authors> listAuth = new ArrayList<>( );
                if(!listOptAuth.isEmpty()) {
                    listAuth = listAuthors.parallelStream().filter(y -> y.getId().equals(s.getId())).collect(Collectors.toList());
                }

                List<Translators> listTrans = new ArrayList<>( );
                if (!listOptTrans.isEmpty()) {
                    listTrans = listTranslator.parallelStream().filter(y -> y.getId().equals(s.getId())).collect(Collectors.toList());

                }

                listDoc.add(Documents.builder()
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
