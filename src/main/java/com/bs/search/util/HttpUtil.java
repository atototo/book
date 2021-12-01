package com.bs.search.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.concurrent.ExecutionException;

@Slf4j
public class HttpUtil {




    public static String  sendGet(String address, String key) throws URISyntaxException, ExecutionException, InterruptedException {
        //http client 생성
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
        String result = client.sendAsync(
                        HttpRequest.newBuilder()
                                .uri(URI.create(address))
                                .header("Authorization", key)
                                .build()
                      ,  HttpResponse.BodyHandlers.ofString()  //응답은 문자형태
                ).thenApply(HttpResponse::body)  //thenApply메소드로 응답body값만 받기
                .get();  //get메소드로 body값의 문자를 확인
        System.out.println(result);

        return result;
    }
}
