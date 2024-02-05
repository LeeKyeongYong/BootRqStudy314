package com.qrstudy.qrstudy.domain.repository.article;

import com.qrstudy.qrstudy.domain.entity.article.Article;
import com.qrstudy.qrstudy.domain.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<Article> findByKw(Board board, String kwType, String kw, Pageable pageable);
}