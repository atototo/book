package com.bs.search.mapper;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.BookEntity.BookEntityBuilder;
import com.bs.search.dto.BookInfoDto;
import com.bs.search.dto.BookInfoDto.BookInfoDtoBuilder;
import com.bs.search.vo.BookApi.Documents;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-01-06T23:00:36+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (Oracle Corporation)"
)
public class DocumentsMapperImpl implements DocumentsMapper {

    @Override
    public BookEntity bookApiToEntity(Documents documentsVO, long id) {
        if ( documentsVO == null ) {
            return null;
        }

        BookEntityBuilder bookEntity = BookEntity.builder();

        if ( documentsVO != null ) {
            bookEntity.title( documentsVO.getTitle() );
            bookEntity.contents( documentsVO.getContents() );
            bookEntity.url( documentsVO.getUrl() );
            bookEntity.isbn( documentsVO.getIsbn() );
            bookEntity.datetime( documentsVO.getDatetime() );
            bookEntity.publisher( documentsVO.getPublisher() );
            bookEntity.price( documentsVO.getPrice() );
            bookEntity.salePrice( documentsVO.getSalePrice() );
            bookEntity.thumbnail( documentsVO.getThumbnail() );
            bookEntity.status( documentsVO.getStatus() );
        }
        bookEntity.id( id );

        return bookEntity.build();
    }

    @Override
    public List<BookInfoDto> bookEntityToBookInfoDto(List<BookEntity> bookEntities) {
        if ( bookEntities == null ) {
            return null;
        }

        List<BookInfoDto> list = new ArrayList<BookInfoDto>( bookEntities.size() );
        for ( BookEntity bookEntity : bookEntities ) {
            list.add( bookEntityToBookInfoDto1( bookEntity ) );
        }

        return list;
    }

    protected BookInfoDto bookEntityToBookInfoDto1(BookEntity bookEntity) {
        if ( bookEntity == null ) {
            return null;
        }

        BookInfoDtoBuilder bookInfoDto = BookInfoDto.builder();

        bookInfoDto.id( bookEntity.getId() );
        bookInfoDto.title( bookEntity.getTitle() );
        bookInfoDto.price( bookEntity.getPrice() );

        return bookInfoDto.build();
    }
}
