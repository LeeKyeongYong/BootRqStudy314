package com.qrstudy.qrstudy.domain.repository.board;

import com.qrstudy.qrstudy.domain.entity.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByCode(String boardCode);
}