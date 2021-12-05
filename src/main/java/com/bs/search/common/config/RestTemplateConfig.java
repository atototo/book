package com.bs.search.common.config;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * packageName : com.bs.search.common.config
 * fileName : RestTemplateConfig
 * author : yelee
 * date : 2021-12-04
 * description : restTemplate 설정
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2021-12-04       yelee         최초 생성
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setReadTimeout(5000); // read timeout
        factory.setConnectTimeout(3000); // connection timeout

        //Apache HttpComponents HttpClient
        HttpClient httpClient = HttpClientBuilder.create()
                                                 .setMaxConnTotal(50)//최대 커넥션 수
                                                 .setMaxConnPerRoute(20).build();
                                                //각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        factory.setHttpClient(httpClient);

        return new RestTemplate(factory);
    }


}
