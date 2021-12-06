# 도서검색하라우

<br/>

카카오 검색 API 를 이용한 검색 서비스개발 / 카카오 키워드 한정 

> * 포트는 8000 을 사용합니다. (application.properties 에서 수정 가능.)

> * 도커 배포 시 포트 5000으로 사용가능합니다. (Dockerfile 에서 수정 가능.)

> * H2 DB는 memory 방식으로 되어있어 서버를 재시작하면 데이타가 초기화 됩니다. 

<hr/>

## SPEC & Tech
### Front-End
`HTML` `BootStrap` `JavaScript` `JQuery` `CSS`
### Back-End
`Java 11` `spring-boot` `h2 (gradle dependency)` `JPA` `Junit`  <br/><br/>
`lombok` `RestTemplate` `HttpEntity` `Ehcache` `Validation` `Advice`



##Build with 
`Gradle`

##Deply with
`Docker`

<hr/>

## 프로젝트 API 문서


| 기능 | 메소드 | 주소 |
| :--- | :---: | ---: |
| 메인화면 렌더링 | GET | /
| 도서검색 | POST | /searhBook


## 프로젝트 실행

### 01) application.yml 설정

* src > main > resources > application.yml에 원하는 포트 설정을 아래와 같은 방식으로 작성해서 이용하시면 됩니다.
기본 포트 `8080` 
* 웹 url : (URL http://localhost:8080/)
* 카카오 API 연동시 원하는 uri와 발급받은 Key를 해당하는 곳에 작성 후 이용하시면 됩니다.
```{.no-highlight}
spring:
  port: 8080
book-api:
  uri: "https://dapi.kakao.com/v3/search/book"
api:
  key: KakaoAK 8c65087a8ceb9dafcbcaefbd5bff3599
  uri: https://dapi.kakao.com/v3/search/book?target=title
```

### 02) Docker Build, Deploy
* Gradle> build > bootjar 실행 후 하단의 명령어로 빌드 합니다.
```{.no-highlight}
docker build -t search-kakao-book .
```
* 도커 이미지 생성 확인
```{.no-highlight}
docker images
```
* 도커 이미지 실행
* 5000포트로 받아서 도커 내부적으로 8080으로 사용하기 위하여 포트를 아래와 같이 설정했습니다 
```{.no-highlight}
docker run -p 5000:8080 search-kakao-book
```
* 배포 완료된 컨테이너 확인
```{.no-highlight}
docker ps
```
