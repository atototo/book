package com.bs.search.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageSearchDto {

    @NotBlank             // null, "" , " " 모두 혀용하지 않는 것
    private String cdSearch;

    @Max(999)
    @Min(0)
    private  int pageNum;

    private int pageSize;
    private String sort;
    private  String title;

    @Max(400000)
    private long minPrice;
    @Max(500000)
    private long maxPrice;

}
