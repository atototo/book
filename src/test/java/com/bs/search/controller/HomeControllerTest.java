package com.bs.search.controller;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.PagingBookRepository;
import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import com.bs.search.vo.BookApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SearchService searchService;

    @MockBean
    private CacheManager cacheManager;

    //들어가는게 맞는지 확인 필요
    @MockBean
    private PagingBookRepository pagingRepository;

    @Test
    @DisplayName("메인 페이지 접속 테스트")
     void test_hello_world() throws Exception {
        /* when */
        final ResultActions resultAction = mockMvc.perform(get("/"));

        /* then */
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("책 조회 파라미터 확인 유효하지 않으면 INVALID_BODY 란 메시지와 함께 status code 400을 리턴한다.")
     void test_searth_books() throws Exception{
        //given
        PageSearchDto pageSearchDto = PageSearchDto.builder()
                                                    .pageNum(0)
                                                    .build();
        //when
        ResultActions perform = mockMvc.perform(post("/searchBooks")
                .content(objectMapper.writeValueAsString(pageSearchDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("INVALID_BODY")));
    }

    @Test
    @DisplayName("검색 테스트")
     void test_search() throws Exception {
        /* given */
        List<String> listAuthors = new ArrayList<>();
        listAuthors.add("안드레아 더리");
        listAuthors.add("토마스 쉬퍼");

        List<String> listTrans = new ArrayList<>();
        listTrans.add("조규희");
        listTrans.add("조규희2");

        BookApi.Documents documents = BookApi.Documents.builder()
                                            .authors(listAuthors)
                                            .contents("초콜릿의 달콤함뿐 아니라, 그 이면에 숨겨진 쌉싸래한 속살을 들춰보는『카카오』. 대중에게 사랑받는 기호식품 초콜릿과 그 원재료인 카카오 사이의 간극을 역사·인문·사회학적 관점에서 다루고 있다. 크게 식물 자체로서의 카카오와 재배 방법/ 카카오 재배농민의 현실과 공정무역/ 카카오의 기원과 메소아메리카 역사/ 유럽 초콜릿 문화사로 나뉜다. 시리즈의 특성답게 한 주제를 다각도로 조명했다는 점, 간결하고 편안한 글투로 흥미를 이끌어 낸다.")
                                            .datetime("2014-08-11T00:00:00.000+09:00")
                                            .isbn("8997429426 9788997429424")
                                            .price(22000)
                                            .publisher("자연과생태")
                                            .salePrice(19800)
                                            .status("정상판매")
                                            .thumbnail("https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1487445%3Ftimestamp%3D20211119163444")
                                            .title("카카오(역사를 바꾼 물질 이야기 5)")
                                            .translators(listTrans)
                                            .url("https://search.daum.net/search?w=bookpage&bookId=1487445&q=%EC%B9%B4%EC%B9%B4%EC%98%A4%28%EC%97%AD%EC%82%AC%EB%A5%BC+%EB%B0%94%EA%BE%BC+%EB%AC%BC%EC%A7%88+%EC%9D%B4%EC%95%BC%EA%B8%B0+5%29")
                                            .build();

        // builder todtjd
        PageSearchDto pageSearchDto = new PageSearchDto();
        pageSearchDto.setCdSearch("bookTitle");
        pageSearchDto.setTitle("카카오");
        pageSearchDto.setPageNum(0);

        //given(searchService.findAllByTarget(pageSearchDto)).willReturn((Page<BookEntity>) documents);


        //stubbing 가정하는 것
        when(searchService.findAllByTarget(pageSearchDto)).thenReturn((Page<BookEntity>) documents);

        /* when -> 실행*/
//        ResultActions resultAction = mockMvc.perform(post("/searchBooks")
//                .accept(MediaType.APPLICATION_JSON_UTF8));

        /* then -> 결과*/
//        resultAction
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("cdSearch").value("bookTitle"))
//                .andExpect(jsonPath("title").value("카카오"))
//                .andDo(MockMvcResultHandlers.print());
    }
}
