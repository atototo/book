package com.bs.search.service;

import com.bs.search.domain.PagingRepository;
import com.bs.search.dto.PageSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * packageName : com.bs.search.service
 * fileName : SearchServiceTest
 * author : isbn8
 * date : 2021-12-04
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       isbn8         최초 생성
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @InjectMocks
    private SearchService searchService;
    @Mock
    private PagingRepository pagingRepository;

    private PageSearchDto pageSearchDto;



    @Test
    void saveDocumentsAll() {
    }

    @Test
    void findAllByTarget() {
    }

    @Test
    void findAllBooksByTitle() {

        pageSearchDto = new PageSearchDto();
        pageSearchDto.setTitle("카카오");
//
//        when(pagingRepository.findByTitleContaining(pageSearchDto.getTitle(), 0)
//                             .thenReturn(Optional.empty());
//
//        ResponseEntity<Page<BookEntity>> list = searchService.findAllBooksByTitle(new StringJoiner(pageSearchDto.getTitle(), "%", "%").toString(), 0);
//
//        assertThrows(BooksNotFoundEception.class, () -> searchService(searchService.findAllBooksByTitle(new StringJoiner(pageSearchDto.getTitle(), "%", "%").toString(), 0)));

    }

}