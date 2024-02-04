package com.qrstudy.qrstudy.domain.repository.member;

import com.qrstudy.qrstudy.domain.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> findByKw(String kwType, String kw, Pageable pageable);
}