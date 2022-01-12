package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.dto.BookInfoDto;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;


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
    @GetMapping("/login")
    public String searchIndex(Model model) {
        return "index.html";
    }

    @GetMapping("/")
    public String searchHome(Model model) {
        // 시큐리티 컨텍스트 객체를 얻습니다.
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("context  : {}", context);
        // 인증 객체를 얻습니다.
         Authentication authentication = context.getAuthentication();

        log.info("authentication  : {}", authentication);


        log.info("principal  : {}",  authentication.getPrincipal());
        // 사용자가 가진 모든 롤 정보를 얻습니다.
         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
         Iterator<? extends GrantedAuthority> iter = authorities.iterator();
         while (iter.hasNext()) {
             GrantedAuthority auth = iter.next();
             System.out.println(auth.getAuthority());
         }



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
    @PostMapping(value = "/search/searchBooks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<BookEntity>> booksListByTarget(@RequestBody @Valid PageSearchDto pageSearchDto) {
        Page<BookEntity> response = searchService.findAllByTarget(pageSearchDto);
        return ResponseEntity.status(200).body(response);
    }
}
