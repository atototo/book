package com.bs.search.common.config;

import com.bs.search.common.KakaoOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * packageName : com.bs.search.common.config
 * fileName : SecurityConfig
 * author : isbn8
 * date : 2022-01-06
 * description :
 * ===========================================================
 * DATE          AUTHOR          NOTE
 * -----------------------------------------------------------
 * 2022-01-06       isbn8         최초 생성
 */

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   private final KakaoOAuth2UserService kakaoOAuth2UserService;

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.oauth2Login().userInfoEndpoint().userService(kakaoOAuth2UserService);
   }
}
