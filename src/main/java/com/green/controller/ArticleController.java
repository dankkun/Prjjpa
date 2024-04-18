package com.green.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.green.dto.ArticleDto;
import com.green.entity.Article;
import com.green.repository.ArticleRepository;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@GetMapping("/articles/WriteForm")
	public String WriteForm() {
		return "articles/write";
	}
	
	@PostMapping("/articles/write")
	public String write( ArticleDto articleDto ) {
		// 넘어온 데이터 확인
		System.out.println( articleDto.toString() );
		// db h2 article 테이블에 저장
		// Entity: db의 테이블이다
		// 1. Dto -> Entity 화
		Article article = articleDto.toEntity();
		// 2. repository(인터페이스)를 사용하여 Entity를 보내서 저장
		Article saved = articleRepository.save( article );

		return "redirect:/articles/list";
	}
	
}
