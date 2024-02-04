package com.qrstudy.qrstudy.domain.repository.member;

import com.qrstudy.qrstudy.domain.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByProducerName(String producerName);
}