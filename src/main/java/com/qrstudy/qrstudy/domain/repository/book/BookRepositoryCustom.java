package com.qrstudy.qrstudy.domain.repository.book;

import com.qrstudy.qrstudy.domain.entity.book.Book;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface BookRepositoryCustom {
    Page<Book> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Book> findByKw(Member author, String kwType, String kw, Pageable pageable);
}