package com.bs.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaInfo {

    private int total_count;				// 페이지 번호
    private int pageable_count;			// 전체 결과 수
    private boolean is_end;			// 한 페이지 결과 수

}
