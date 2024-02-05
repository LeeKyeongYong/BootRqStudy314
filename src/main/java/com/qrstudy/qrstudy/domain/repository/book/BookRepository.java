package com.qrstudy.qrstudy.domain.repository.book;

import com.qrstudy.qrstudy.domain.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    Page<Book> findByBookTags_content(String tagContent, Pageable pageable);
}