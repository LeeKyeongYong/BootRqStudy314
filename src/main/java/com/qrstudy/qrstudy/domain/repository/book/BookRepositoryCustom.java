package com.qrstudy.qrstudy.domain.repository.book;

import org.springframework.data.domain.Page;

import java.awt.print.Book;

public interface BookRepositoryCustom {
    Page<Book> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Book> findByKw(Member author, String kwType, String kw, Pageable pageable);
}