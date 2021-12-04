package com.bs.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * packageName : com.bs.search.dto
 * fileName : PageSearchDto
 * author : yelee
 * date : 2021-12-03
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-03       isbn8         최초 생성
 */
@ToString
@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageSearchDto {

    @NotBlank
    private String cdSearch;

    @Max(999)
    @Min(0)
    private  int pageNum;

    private int pageSize;
    private String sort;
    private  String title;

    private int minPrice;
    @Max(500000)
    private int maxPrice;
}
