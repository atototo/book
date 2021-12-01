package com.bs.search.common.config;

import lombok.extern.slf4j.Slf4j;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Slf4j
@Configuration
public class TemplateConfig {

	@Value("${thymeleaf.cache}")
	private boolean isCache;

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		var templateResolver = new SpringResourceTemplateResolver();
		var views = "classpath:templates/views/";
		templateResolver.setPrefix(views);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(isCache);

		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(MessageSource messageSource) {
		var templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setTemplateEngineMessageSource(messageSource);
		templateEngine.addDialect(layoutDialect());

		return templateEngine;
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	@Autowired
	public ViewResolver viewResolver(MessageSource messageSource) {
		var viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine(messageSource));
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setOrder(0);

		return viewResolver;
	}
}
