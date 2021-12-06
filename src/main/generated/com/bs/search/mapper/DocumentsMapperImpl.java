package com.bs.search.mapper;

import com.bs.search.domain.BookEntity;
import com.bs.search.domain.BookEntity.BookEntityBuilder;
import com.bs.search.vo.BookApi.Documents;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-06T17:35:38+0900",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.12 (Oracle Corporation)"
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
}
