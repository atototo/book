package com.bs.search.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * packageName : com.bs.search.domain
 * fileName : AuthorsEntityTest
 * author : yelee
 * date : 2021-12-05
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-05       isbn8         최초 생성
 */
class AuthorsEntityTest {

    private AuthorsEntity authorsEntity;

    @Test
    @DisplayName("저작권자 등록 필수 값 확인")
    void createAuthosWithEssentialTest() {
        /*given*/
        long id = 0;

        AuthorsEntity authorsEntity = AuthorsEntity.builder()
                .authorId(null)
                .title("카카오")
                .id(1L)
                .author("조희수")
                .build();

        // then
//        assertThatThrownBy(() -> authorsEntity.getAuthor().valueOf(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("isbn 의 값이 없습니다.");
//        assertThat(authorsEntity.getAuthor_id()).isEqualTo(id);
//        assertThat(book.getTitle()).isEqualTo("Spring");
//        assertThat(book.getStockQuantity().getValue()).isEqualTo(1);
//        assertThat(book.getRegistrationDate()).isEqualTo(registrantDateTime);

    }

}