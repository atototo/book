package com.bs.search.controller;

import com.bs.search.domain.Documents;
import com.bs.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    private SearchService searchService;

    @GetMapping(value = "/books/title/{title}")
    public ResponseEntity<List> booksByTitle(@PathVariable(value = "title") String title) {
        log.info("title 아 어디갔니이 : {}", title);
        List<Documents> listDoc =  searchService.findAllDocumentsByTitle(title);

        if(listDoc.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(new String[]{"데이터 없음."}));
        }
        return ResponseEntity.status(200).body(listDoc);
    }
}
