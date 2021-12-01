package com.bs.search.controller;

import com.bs.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    private SearchService searchService;

//    @GetMapping(value = "/books/title/{title}")
//    public ResponseEntity<List> booksByTitle(@PathVariable(value = "title") String title) {
//        log.info("title 아 어디갔니이 : {}", title);
//        List<DocumentsDto> listDoc =  searchService.findAllDocumentsByTitle(title);
//
//        if(listDoc.size() == 0){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(new String[]{"데이터 없음."}));
//        }
//        return ResponseEntity.status(200).body(listDoc);
//    }
}
