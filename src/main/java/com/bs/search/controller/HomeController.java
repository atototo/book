package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.PagingRepository;
import com.bs.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private SearchService searchService;


    @Autowired
    private PagingRepository pagingRepository;


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


    /**
     * methodName : searchBookByTitle
     * author : yelee
     * description :
     *
     * @param title
     * @param page     num
     * @param response
     * @param pageable
     * @return response entity
     * @throws IOException the io exception
     */
    @GetMapping("/searchBookByTitle")
    @ResponseBody
    public ResponseEntity<Page<BookEntity>> searchBookByTitle(@RequestParam String title,@RequestParam String pageNum,  HttpServletResponse response
                                                    ,@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        log.info("title  :: {}", title);

        Pageable page = PageRequest.of(Integer.parseInt(pageNum),10);
        Page<BookEntity> blist = pagingRepository.findAllByTitle(title, page);
        return ResponseEntity.status(200).body(blist);
    }

    @GetMapping("/searchBookByPrice")
    @ResponseBody
    public  ResponseEntity<Page<BookEntity>> booksListByPrice(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,

                                              @RequestParam String min, @RequestParam String max,  @RequestParam String pageNum) {
       Pageable page = PageRequest.of(Integer.parseInt(pageNum), 10);
        Page<BookEntity> blist = pagingRepository.findAllByPriceBetween(Long.parseLong(min),Long.parseLong(max), page);


         return  ResponseEntity.status(200).body(blist);
    }
}
