package com.qrstudy.qrstudy.domain.repository.member;

import com.qrstudy.qrstudy.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);
}
