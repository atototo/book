package com.bs.search.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    private BookEntity bookEntity;

    private static String title = "카카오(역사를 바꾼 물질 이야기 5)";


    @BeforeEach
    void setUp() {
//        /* given */
//        List<String> listAuthors = new ArrayList<>();
//        listAuthors.add("안드레아 더리");
//        listAuthors.add("토마스 쉬퍼");
//
//        List<String> listTrans = new ArrayList<>();
//        listTrans.add("조규희");
//        listTrans.add("조규희2");
        bookEntity = BookEntity.builder()
                .id(1L)
                .contents("초콜릿의 달콤함뿐 아니라, 그 이면에 숨겨진 쌉싸래한 속살을 들춰보는『카카오』. 대중에게 사랑받는 기호식품 초콜릿과 그 원재료인 카카오 사이의 간극을 역사·인문·사회학적 관점에서 다루고 있다. 크게 식물 자체로서의 카카오와 재배 방법/ 카카오 재배농민의 현실과 공정무역/ 카카오의 기원과 메소아메리카 역사/ 유럽 초콜릿 문화사로 나뉜다. 시리즈의 특성답게 한 주제를 다각도로 조명했다는 점, 간결하고 편안한 글투로 흥미를 이끌어 낸다.")
                .datetime("2014-08-11T00:00:00.000+09:00")
                .isbn("8997429426 9788997429424")
                .price(22000)
                .publisher("자연과생태")
                .salePrice(19800)
                .status("정상판매")
                .thumbnail("https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1487445%3Ftimestamp%3D20211119163444")
                .title("카카오(역사를 바꾼 물질 이야기 5)")
                .build();
    }

    @Test
    @DisplayName("조회된 도서 정보 저장 확인 테스트")
    void saveAll(){
        // given
        List<BookEntity> bookList = new ArrayList<>();
        bookList.add(bookEntity);

        // when
        bookRepository.saveAll(bookList);

        // then
        assertThat(bookRepository.findById(bookEntity.getId()))
                .isNotEmpty();


    }

    //도서정보 저장시 id가 널이면 안됨
    @Test
    @DisplayName("도서정보 저장시 ID는 필수 정보")
    void saveWithoutId() {
        // given
        Long id = 111L;

        // when
        BookEntity book = BookEntity.builder()
                                .isbn("test isbn")
                                .title("kakao")
                                .build();

        assertThatThrownBy(() -> bookRepository.save(book))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("책 id가 null일 수 없습니다.");
    }

}