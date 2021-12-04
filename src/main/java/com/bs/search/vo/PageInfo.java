package com.bs.search.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * packageName : com.bs.search.vo
 * fileName : PageInfo
 * author : isbn8
 * date : 2021-12-05
 * description : API 조회 시 페이지 고정 정보 사용 용도
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-05       isbn8         최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 8375164265167554225L;

    private int reqChkPageCnt = 5;
    private int reqMaxCnt = 50;

    private int totalCnt;
    private int reqApiCnt;


    public PageInfo(int totalCnt){
        this.totalCnt = totalCnt;
    }

    public int getReqApiCnt() {
        return  this.totalCnt%reqMaxCnt> 0? (this.totalCnt / reqMaxCnt )+1 : (this.totalCnt / reqMaxCnt );
    }

    public enum ChkCnt {
        REQ_CHK_PAGE_CNT(5),
        REQ_MAX_CNT(50),
        REQ_DEFAULT_PAGE_SIZE(10);

        private int cnt;

        ChkCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getCnt() {
            return cnt;
        }
    }
}
