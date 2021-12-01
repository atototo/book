package com.bs.search.service;

import com.bs.search.domain.BookApi;
import com.bs.search.util.HttpUtil;
import com.bs.search.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.uri}")
    private String uri;

    @Value("${api.auth}")
    private String auth;

    @Value("${api.head}")
    private String headOption;

    @Transactional
    public void selectAll() throws Exception {
            String paramUrl = "&query=카카오";
           String result =  HttpUtil.sendGet(uri+paramUrl,  apiKey);

        BookApi bookApi = (BookApi) JsonUtil.jsonStrToObject(result, new BookApi());

        log.info("bookApi :: {}", bookApi.toString());
    }

}
