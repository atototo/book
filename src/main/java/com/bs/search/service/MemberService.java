package com.bs.search.service;

import com.bs.search.domain.Member;
import com.bs.search.domain.MemberRepository;
import com.bs.search.vo.BookApiVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final RestTemplate restTemplate;
    private final MemberRepository repository;

    @Value("${book-api.uri}")
    private String bookApiUri;

    public void save() {
        repository.save(Member.builder().name("liu").build());
    }


    public void saveAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("header", "header");
        headers.set("header2", "header2");
        HttpEntity request = new HttpEntity(headers);
        //adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(bookApiUri).queryParam("keywords", "11").queryParam("name", "22");
        ResponseEntity<BookApiVO> response = restTemplate.exchange( uriBuilder.toUriString(), HttpMethod.GET, request, BookApiVO.class );

        LinkedList<Member> list = new LinkedList<>();
        list.add(Member.builder().name("liu").build());
        list.add(Member.builder().name("llllu1").build());
        list.add(Member.builder().name("llllu2").build());

        repository.saveAll(list);
    }

    public List findAll() {
        return Collections.singletonList(repository.findAll());
    }

    public List findAllByName(String name) {
        return repository.findAllByNameOrderByName(name);
    }

}
