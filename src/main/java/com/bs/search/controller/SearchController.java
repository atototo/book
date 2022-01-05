package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.dto.BookInfoDto;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * methodName : searchHome
     * author : yelee
     * description : 메인페이지 이동
     *
     * @param model
     * @return string
     */
    @GetMapping("/")
    public String searchHome(Model model) {
        return "search.html";
    }

    /**
     * methodName : booksListByTarget
     * author : yelee
     * description : 조건에 따른 도서목록 조회
     *
     * @param pageSearchDto
     * @return
     */
    @PostMapping(value = "/searchBooks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<BookEntity>> booksListByTarget(@RequestBody @Valid PageSearchDto pageSearchDto) {
        Page<BookEntity> response = searchService.findAllByTarget(pageSearchDto);
        return ResponseEntity.status(200).body(response);
    }
}
