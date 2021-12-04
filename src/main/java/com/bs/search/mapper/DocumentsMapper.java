package com.bs.search.mapper;

import com.bs.search.domain.BookEntity;
import com.bs.search.vo.BookApi;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


/**
 * packageName : com.bs.search.mapper
 * fileName : DocumentsMapper
 * author : yelee
 * date : 2021-12-04
 * description : BookApi.Documents 를 Book Entity 매핑
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentsMapper {
    DocumentsMapper INSTANCE = Mappers.getMapper(DocumentsMapper.class);

    BookEntity bookApiToEntity(BookApi.Documents documentsVO, long id);


}
