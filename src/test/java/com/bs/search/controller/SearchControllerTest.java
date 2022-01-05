package com.bs.search.controller;

import com.bs.search.common.PageSearchEnum;
import com.bs.search.dto.PageSearchDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * packageName : com.bs.search.controller
 * fileName : SearchControllerTest
 * author : isbn8
 * date : 2021-12-06
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-06       isbn8         최초 생성
 */
@SpringBootTest
class SearchControllerTest {

    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void tearDown() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("메인페이지 접근 확인")
    void searchHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서검색확인 현재 데이터 없기 때문에 BookNotFoundException 발생")
    void booksListByTarget() throws Exception {

        val pageSearchDto = PageSearchDto.builder()
                .pageNum(1)
                .pageSize(1)
                .cdSearch(PageSearchEnum.SEARCH_BY_TITLE.getValue())
                .title("카카오")
                .build();


        ObjectMapper mapper = new ObjectMapper();
         String json = mapper.writeValueAsString(pageSearchDto);
        mockMvc.perform(
                post("/searchBooks")
                        .content(json)
                        .contentType("application/json")
                        .accept("application/json")
        ).andExpect(status().isBadRequest());
        //데이터 밀어넣지 않아서 검색결과 없는  error
    }


    @Test
    @DisplayName("도서검색확인 valid 검증 에러 발생")
    void booksListByTargetValidFail() throws Exception {

        val pageSearchDto = PageSearchDto.builder()
                .pageNum(1)
                .pageSize(1)
                .cdSearch(PageSearchEnum.SEARCH_BY_PRICE_BETWEEN.getValue())
                .minPrice(500000)
                .build();


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(pageSearchDto);

        mockMvc.perform(
                post("/searchBooks")
                        .content(json)
                        .contentType("application/json")
                        .accept("application/json")
        ).andExpect(status().isBadRequest());
        //데이터 밀어넣지 않아서 검색결과 없는  error
    }
}