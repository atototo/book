//package com.bs.search.mapper;
//
//import com.bs.search.domain.BookEntity;
//import com.bs.search.vo.BookApi;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//class DocumentsMapperTest {
//
//    private static BookApi.Documents documents;
//
//    void setTestData() {
//        List<String> listAuthors = new ArrayList<>();
//        listAuthors.add("안드레아 더리");
//        listAuthors.add("토마스 쉬퍼");
//
//        List<String> listTrans = new ArrayList<>();
//        listTrans.add("조규희");
//        listTrans.add("조규희2");
//
//
//
//       documents = BookApi.Documents.builder()
//                .authors(listAuthors)
//                .contents("초콜릿의 달콤함뿐 아니라, 그 이면에 숨겨진 쌉싸래한 속살을 들춰보는『카카오』. 대중에게 사랑받는 기호식품 초콜릿과 그 원재료인 카카오 사이의 간극을 역사·인문·사회학적 관점에서 다루고 있다. 크게 식물 자체로서의 카카오와 재배 방법/ 카카오 재배농민의 현실과 공정무역/ 카카오의 기원과 메소아메리카 역사/ 유럽 초콜릿 문화사로 나뉜다. 시리즈의 특성답게 한 주제를 다각도로 조명했다는 점, 간결하고 편안한 글투로 흥미를 이끌어 낸다.")
//                .datetime("2014-08-11T00:00:00.000+09:00")
//                .isbn("8997429426 9788997429424")
//                .price(22000)
//                .publisher("자연과생태")
//                .salePrice(19800)
//                .status("정상판매")
//                .thumbnail("https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1487445%3Ftimestamp%3D20211119163444")
//                .title("카카오(역사를 바꾼 물질 이야기 5)")
//                .translators(listTrans)
//                .url("https://search.daum.net/search?w=bookpage&bookId=1487445&q=%EC%B9%B4%EC%B9%B4%EC%98%A4%28%EC%97%AD%EC%82%AC%EB%A5%BC+%EB%B0%94%EA%BE%BC+%EB%AC%BC%EC%A7%88+%EC%9D%B4%EC%95%BC%EA%B8%B0+5%29")
//                .build();
//    }
//
//    @Test
//    @DisplayName("BookApi.Documnets to Entity TEST")
//    void test_documents_to_bookentity_event() {
//        /* given */
//        setTestData();
//        /* when */
//        final BookEntity bookEntity = DocumentsMapper.INSTANCE.bookApiToEntity(documents, 1);
//        /* then */
//        assertNotNull(bookEntity);
//        assertThat(bookEntity.getTitle()).isEqualTo("카카오(역사를 바꾼 물질 이야기 5)");
//        assertThat(bookEntity.getId()).isEqualTo(1);
//        assertThat(bookEntity.getStatus()).isNotEmpty();
//        assertThat(bookEntity.getPrice()).isEqualTo(22000);
////        assertThat(order.getAddress()).isEqualTo("Seoul");
////        assertThat(order.getOrderedTime()).isBefore(LocalDateTime.now());
////        assertThat(order.getId()).isEqualTo(0L);
//    }
//
//}