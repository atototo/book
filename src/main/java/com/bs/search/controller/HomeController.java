package com.bs.search.controller;

import com.bs.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/")
    public String helloWorld(Model model){
        log.info("home controller");

        return "search";
    }


    @GetMapping("/searchBookByTitle")
    @ResponseBody
    public ResponseEntity<List<?>> searchBookByTitle(@RequestParam String title, HttpServletResponse response) throws IOException {
        log.info("title  :: {}", title);
      return searchService.fildAllBooksByTitle(title);
    }

    @GetMapping("/searchBookByBookId")
    @ResponseBody
    public ResponseEntity<List<?>> searchBookByBookId(@RequestParam String title, HttpServletResponse response) throws IOException {
      return searchService.fildAllBooksById(title);
    }
}
