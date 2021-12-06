package com.bs.search.service;


import com.bs.search.domain.BookEntity;
import com.bs.search.domain.PagingBookRepository;
import com.bs.search.dto.PageSearchDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * packageName : com.bs.search.service
 * fileName : SearchServiceTest
 * author : isbn8
 * date : 2021-12-06
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-06       isbn8         최초 생성
 */
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private PagingBookRepository pagingBookRepository;

    @Test
    @DisplayName("도서 목록 조회 like 조회 확인")
    void findAllByTitleLike() {

        when(pagingBookRepository.findByTitleContaining(eq("프렌즈"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Lists.newArrayList(BookEntity.builder().title("카카오 프렌즈 1").build())));

        Page<BookEntity> response = searchService.findAllByTitleLike(mockPageDto(10000, 20000, 0, 10));

        assertThat(response.getNumberOfElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("카카오 프렌즈 1");

    }

    private PageSearchDto mockPageDto(long min, long max, int pageNum, int pageSize) {
        return PageSearchDto.builder().title("프렌즈").minPrice(min).maxPrice(max).pageNum(pageNum).pageSize(pageSize).build();
    }


}