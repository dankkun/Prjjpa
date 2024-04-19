package com.green.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 실제 database의 table 구조를 만든다 (Create table)
@Entity
@NoArgsConstructor   // 기본생성자: default constructor. 이거쓰면 기본생성자는 따로 안만들어도 됌 // 롬복이 있기 때문에 먹힘
@Getter
@Setter
public class Article {
	// primary key: @id
	// 값을 자동으로 채움: @GeneratedValue
	@Id
	@GeneratedValue
	private Long id;  // long: null 입력안됨 -> null 값 입력할 수도 있는 db스타일에 맞게 Long 타입으로 설정
	@Column
	@Nonnull
	private String title;
	@Column
	private String content;
	
	//생성자
	public Article(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	// toString
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content=" + content + "]";
	}
	
	
	
}
