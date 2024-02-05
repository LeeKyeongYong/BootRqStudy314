package com.qrstudy.qrstudy.domain.repository.post;

import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable);
}