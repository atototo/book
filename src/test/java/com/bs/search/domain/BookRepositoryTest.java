package com.bs.search.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : com.bs.search.domain
 * fileName : BookRepositoryTest
 * author : isbn8
 * date : 2021-12-05
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-05       isbn8         최초 생성
 */
@Transactional
@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    void makeData() {
        //given
        List<BookEntity> bookList = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            BookEntity bookEntity = BookEntity.builder()
                    .id((long) i)
                    .price((5000L * i))
                    .title("카카오_" + i)
                    .build();
            bookList.add(bookEntity);
        });

        //when
        bookRepository.saveAll(bookList);
    }

    @Test
    @DisplayName("조회된 도서 정보 저장 확인 테스트")
    void saveAll(){
        //given, when
        makeData();

        //then
        assertAll(
                () -> assertThat(bookRepository.findById(1L)).isNotEmpty(),
                () -> assertThat(bookRepository.findById(5L)).isNotEmpty(),
                () -> assertThat(bookRepository.findById(10L)).isNotEmpty()
        );

    }

    @Test
    @DisplayName("도서정보 저장시 ID는 필수 정보")
    void saveWithoutId() {
        // given
        Long id = null;

        // when
        BookEntity book = BookEntity.builder()
                                .id(id)
                                .isbn("test isbn")
                                .title("kakao")
                                .build();

        assertThatThrownBy(() -> bookRepository.save(book))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("책 id가 null일 수 없습니다.");
    }

}