package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.PagingBookRepository;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


/**
 * packageName : com.bs.search.controller
 * fileName : SearchController
 * author : yelee
 * date : 2021-12-04
 * description : 기본 컨트롤러
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    private final PagingBookRepository pagingRepository;

    private final CacheManager cacheManager;

    /**
     * methodName : helloWorld
     * author : yelee
     * description : 메인함수 이동
     *
     * @param model
     * @return string
     */
    @GetMapping("/")
    public String helloWorld(Model model) {
        log.info("home controller");

        return "search.html";
    }

    @PostMapping(value = "/searchBooks" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public  ResponseEntity<Page<BookEntity>> booksListByTarget(@RequestBody @Valid PageSearchDto pageSearchDto) {
        long start = System.currentTimeMillis(); // 수행시간 측정

       Page<BookEntity> response = searchService.findAllByTarget(pageSearchDto);
        long end = System.currentTimeMillis();
        log.info("{},  수행시간 , : {} ms",pageSearchDto.getTitle(),(end-start) );
        return  ResponseEntity.status(200).body(response);
    }
}
