package com.bs.search.mapper;

import com.bs.search.domain.BookEntity;
import com.bs.search.vo.BookApiVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentsMapper {
    DocumentsMapper INSTANCE = Mappers.getMapper(DocumentsMapper.class);

    BookEntity bookApiVOToEntity(BookApiVO.Documents documentsVO, long id);


}
