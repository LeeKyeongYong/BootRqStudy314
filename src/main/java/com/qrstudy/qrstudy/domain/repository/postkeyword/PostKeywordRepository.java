package com.qrstudy.qrstudy.domain.repository.postkeyword;


import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.postkeyword.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    Optional<PostKeyword> findByAuthorAndContent(Member author, String content);

    List<PostKeyword> findByAuthorOrderByContent(Member actor);
}