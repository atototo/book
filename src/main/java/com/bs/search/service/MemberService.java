package com.bs.search.service;

import com.bs.search.domain.Book;
import com.bs.search.domain.BookRepository;
import com.bs.search.domain.Member;
import com.bs.search.domain.MemberRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final RestTemplate restTemplate;
    private final MemberRepository repository;
    private final BookRepository bookRepository;

    @Value("${api.uri}")
    private String bookApiUri;

    @Value("${api.key}")
    private String key;



    public void save() {
        repository.save(Member.builder().name("liu").build());
    }


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
        //false 일 경우 페이지 증가하여 재호출
        boolean isEnd =  response.getBody().getMeta().isEnd();
        log.info("totalCount : {} ",totalCnt);
        log.info("isEnd : {} ",isEnd);

        HashMap<Integer,ArrayList<BookApiVO.Documents>> docMap = new HashMap<Integer,ArrayList<BookApiVO.Documents>>();
        int reqCnt = 1;
        while (totalCnt > 0) {
            UriComponents  reUri = UriComponentsBuilder.fromHttpUrl(bookApiUri)
                    .queryParam("target", "title")
                    .queryParam("query", "카카오")
                    .queryParam("page",  reqCnt)
                    .queryParam("size", 50)
                    .build();
            ResponseEntity<BookApiVO>   resApi = restTemplate.exchange( reUri.toUri(), HttpMethod.GET, request, BookApiVO.class );
            ArrayList<BookApiVO.Documents>  listDoc = (ArrayList<BookApiVO.Documents>) resApi.getBody().getDocuments();
//            docSet.add(listDoc);
            docMap.put(reqCnt,listDoc ) ;
            reqCnt+=1;
            totalCnt = totalCnt - listDoc.size();
            isEnd = resApi.getBody().getMeta().isEnd();
            log.info("isEnd totalCnt : {} ",totalCnt);
        }

        ArrayList<Book> listBook = new ArrayList<Book>();
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
                        .build()
                );
            });
        });
//        docMap..forEach( t -> {
//            t.stream().forEach(s -> {
//                listBook.add(Book.builder()
//                        .title(s.getTitle())
//                        .contents(s.getContents())
//                        .url(s.getUrl())
//                        .isbn(s.getUrl())
//                        .datetime(s.getDatetime())
//                        .price(s.getPrice())
//                        .salePrice(s.getSalePrice())
//                        .thumbnail(s.getThumbnail())
//                        .status(s.getStatus())
//                        .build()
//                );
//            });
//        });

        bookRepository.saveAll(listBook);
        log.info("api response meta : {}", response.getBody().getMeta().toString());
        log.info("api response meta : {}", response.getBody().getMeta().getTotalCount());

    }

    public List findAll() {
        return Collections.singletonList(repository.findAll());
    }

    public List findAllByName(String name) {
        return repository.findAllByNameOrderByName(name);
    }

}
