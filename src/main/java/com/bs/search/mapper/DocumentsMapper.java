package com.bs.search.mapper;

import com.bs.search.domain.BookEntity;
import com.bs.search.vo.BookApi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DocumentsMapper {
    DocumentsMapper INSTANCE = Mappers.getMapper(DocumentsMapper.class);

    BookEntity bookApiToEntity(BookApi.Documents documentsVO, long id);


}
