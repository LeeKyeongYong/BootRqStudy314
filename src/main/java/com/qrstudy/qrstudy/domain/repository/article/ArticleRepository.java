package com.qrstudy.qrstudy.domain.repository.article;

import com.qrstudy.qrstudy.domain.entity.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    Page<Article> findByArticleTags_content(String tagContent, Pageable pageable);
}