package com.rootlab.simpleboard.repository;

import com.rootlab.simpleboard.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	// User model의 boards 멤버에 설정된 fetch = FetchType.LAZY로 인해 불필요하게 쿼리문이 나뉘게 됨
	// @EntityGraph를 사용해서 데이터를 한번에 가져올 수 있도록 함
	@EntityGraph(attributePaths = {"boards"})
	List<User> findAll();

	User findByUsername(String username);
}
