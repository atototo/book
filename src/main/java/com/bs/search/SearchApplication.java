package com.bs.search;

import com.bs.search.service.SearchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication	//스프링이부튼의 가장 기본적인 설정을 선언해준다.
//@ServletComponentScan //서블릿 Component(@WebFilter,@WebServlet, @WebListener)를 스캔할 때 사용하는 어노테이션으로 사실상 없어도 된다.
public class SearchApplication {
	public static void main(String[] args) {
		// Spring Boot 의 기동은 SpringApplication 클래스를 사용한다. 가장 간단한 방법은 SpringApplication#run (Object, String ...)을 사용하는 것
		ApplicationContext ctx = SpringApplication.run(SearchApplication.class, args);
		//특정서비스 실행위해 스프링컨테이너에서 SearchService 빈 받아와서  실행
		SearchService searchService = ctx.getBean(SearchService.class) ;
		searchService.saveDocumentsAllNew();
	}

	/**
	 * 스프링 부트는 run() 이라는 콜백 메소드를 가진 CommendLineRunner라는 인터페이스 제공
	 * run() 메소드는 Spring application context의 초기화가 완료된(모든 Baan이 초기화된) 후에 실행되므로
	 * 이 안에 원하는 로직을 작성하면 된다.
	 */
//	@Bean
//	public CommandLineRunner run(SearchService searchService) {
//		return (String[] args) -> searchService.saveDocumentsAll();
//	}

}
