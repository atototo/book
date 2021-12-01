package com.bs.search;

import com.bs.search.service.MemberService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class SearchApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SearchApplication.class, args);
		MemberService memberService = ctx.getBean(MemberService.class) ;
		memberService.saveAll();
	}
}
