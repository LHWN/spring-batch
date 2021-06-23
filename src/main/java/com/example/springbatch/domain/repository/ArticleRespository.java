package com.example.springbatch.domain.repository;

import com.example.springbatch.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ArticleRespository extends JpaRepository<Article, Long> {
}
