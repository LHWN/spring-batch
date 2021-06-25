package com.example.springbatch.domain.repository;

import com.example.springbatch.domain.entity.Article;
import com.example.springbatch.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRespository extends JpaRepository<Board, Long> {
}
