package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.PagingRepository;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private PagingRepository pagingRepository;

    @Autowired
    CacheManager cacheManager;

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

    @PostMapping(value = "/searchBooks")
    @ResponseBody
    public  ResponseEntity<Page<BookEntity>> booksListByTarget(@RequestBody @Valid PageSearchDto pageSearchDto) {
        long start = System.currentTimeMillis(); // 수행시간 측정

       Page<BookEntity> response = searchService.findAllByTarget(pageSearchDto);
        long end = System.currentTimeMillis();
        log.info("{},  수행시간 , : {} ms",pageSearchDto.getTitle(),(end-start) );
        return  ResponseEntity.status(200).body(response);
    }
}
