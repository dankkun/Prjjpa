package com.green.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.green.dto.ArticleDto;
import com.green.dto.ArticleForm;
import com.green.entity.Article;
import com.green.repository.ArticleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ArticleController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	// data 입력창
	@GetMapping("/articles/writeform")
	public String WriteForm() {
		return "articles/write";
		// src/main/resources/templates/articles/write.mustache
	}
	
	// data 저장
	@PostMapping("/articles/write")
	public String write( ArticleDto articleDto ) {
		// 넘어온 데이터 확인
		System.out.println( articleDto.toString() );
		// db h2 article 테이블에 저장
		// Entity: db의 테이블이다
		// 1. Dto -> Entity 화
		Article article = articleDto.toEntity();
		// 2. repository(인터페이스)를 사용하여 Entity를 보내서 저장 / jpa가 .save() 함수로 'insert'
		Article saved = articleRepository.save( article );
		System.out.println("saved:" + saved);
		return "redirect:/articles/list";
	}
	
	// 1번 데이터 조회: PathVariable -> GET
	
	// java.lang.IllegalArgumentException: Name for argument of type [java.lang.Long] not specified, ... 에러 ->
	// 1번 방법. @PathVariable(value="id") 추가
	// 2번 방법. sts 설정 추가
	// 프로젝트 -> ✅ properties -> Java Compiler -> Enable project specific settings 체크 
	//          -> ✅ 가장 밑줄 Store information about method parameters (usable via reflection) 체크
	
	// No default constructor for entity 'com.green.entity.Article'...  에러 (기본 생성자가 없다) ->
	// Article에 @NoArgsConstructor 추가
	@GetMapping("/articles/{id}")
	public String view(@PathVariable(value="id") Long id, Model model) {
		
		// Article articleEntity = articleRepository.findById(id);
		// Type mismatch error 해결방법 1번
		// Optional<Article> articleEntity = articleRepository.findById(id);
		// 값이 있으면 Article을 리턴, 값이 없으면 null 리턴
		
		// 2번방법
		Article articleEntity = articleRepository.findById(id).orElse(null);
		System.out.println("1번 조회 결과:" + articleEntity);
		model.addAttribute("article", articleEntity ); // 조회한 결과 -> model
		System.out.println("model.addAttribute(\"article\", articleEntity ); 값:" + model);

		return "articles/view"; // articles/view.mustache
	}
	
	@GetMapping("/articles/list")
	public String list(Model model) {
		
		// List<Article> articleEntityList = (List<Article>) articleRepository.findAll();
		
		List<Article> articleEntityList = articleRepository.findAll();
		System.out.println("전체목록:" + articleEntityList);
		model.addAttribute("articlelist", articleEntityList);
		return "articles/list";
	}
	
	// 데이터 수정페이지로 이동
	@GetMapping("/articles/{id}/editform")
	public String editform(@PathVariable Long id, Model model) {
		// 수정할 데이터를 조회한다
		Article articleEntity = articleRepository.findById(id).orElse(null);
		
		// 조회한 데이터를 model에 저장
		model.addAttribute("articleEntity", articleEntity);
		
		// 수정페이지로 이동
		return "/articles/edit";
	}
	
	// 데이터 수정
	@PostMapping("/articles/edit")
	public String edit( ArticleForm articleForm ) {
		log.info("수정용 데이터:" + articleForm.toString());
		// db 수정
		// 1. DTO -> Entity로 변환
		Article articleEntity = articleForm.toEntity();   // articleForm 클래스 안에 메소드 toEntity를 이미 설정해뒀기 때문에 사용가능
		// 2. entity를 db에 수정한다
		// 2-1. 수정할 데이터를 찾아서 (db의 data를 가져온다)
		Long id = articleForm.getId();
		Article target = articleRepository.findById(id).orElse(null);
		// 2-2. 필요한 데이터를 변경한다
		if(target != null) {  // 자료가 있으면 저장한다(수정)
			articleRepository.save(articleEntity);
		}
		return "redirect:/articles/list";
	}
	
	// 데이터 삭제
	@GetMapping("/articles/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes  rttr) {
		log.info("삭제 요청이 들어왔습니다!");
		
		// 1. 삭제할 대상 가져오기
		Article target = articleRepository.findById(id).orElse(null); 
		log.info( target.toString() );
		
		// 2. 대상 엔티티 삭제하기
		if( target != null) {
			articleRepository.delete(target);
		}
		
		// RedirectAttributes  rttr: 리다이렉트 페이지에서 사용할 데이터를 넘김(한번 쓰면 사라지는 휘발성 데이터)
		// 삭제 후 임시 메시지를 list.mustache가 출력한다
		
		rttr.addFlashAttribute("msg", id + "번 자료가 삭제되었습니다");
		
		return "redirect:/articles/list";
	}
	
	
}
