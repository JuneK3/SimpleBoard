package com.rootlab.simpleboard.repository;

import com.rootlab.simpleboard.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
