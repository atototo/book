package com.bs.search.domain;

import com.bs.search.dto.PageSearchDto;
import com.bs.search.service.SearchService;
import com.bs.search.vo.BookApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * packageName : com.bs.search.domain
 * fileName : PagingRepositoryTest
 * author : yelee
 * date : 2021-12-05
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-05       yelee         최초 생성
 */
@SpringBootTest
class PagingRepositoryTest {

    @Autowired
    private PagingBookRepository pagingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SearchService searchService;

    //like 조회가 잘 되는지
    @DisplayName("타이틀로 검색 할 때 라이크 조회잘 되니")
    @Test
    void findByTitleContaining() {

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



        // given
        String title = "엽서";
        Pageable page = (Pageable) PageRequest.of(0, 10);


        Page<BookEntity> listBook = pagingRepository.findByTitleContaining(title, (Pageable) page);
        if (listBook == null) throw new NullPointerException();
        assertNotNull(listBook);
        Optional.of(listBook).ifPresent(
                a -> a.getContent().forEach(s -> {
//                    assertThat(s.getTitle().contains("선물")).isFalse();
                    assertTrue("같아", s.getTitle().contains("선물"));
                })
        );

    }

}