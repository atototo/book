package com.bs.search.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * packageName : com.bs.search.dto
 * fileName : PageSearchDtoTest
 * author : isbn8
 * date : 2021-12-03
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-03       isbn8         최초 생성
 */
@Slf4j
class PageSearchDtoTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("페이지 DTO 필수 값 확인")
    void lombokTest(){
        //given
//        String title = "test";
//        int pageNum = 1;
//        //when
//        PageSearchDto dto = new PageSearchDto();

        //then
//        assertThat(dto.getTitle()).isEqualTo(title); //(1) (2) ❓
//        assertThat(dto.getPageNum()).isEqualTo(pageNum);

    }

    //book타이틀로 조회 했을 때 해당 책의 저작권자와 번역자가 같이 나와야 됨  이게 제일 관건
}