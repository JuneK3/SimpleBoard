package com.rootlab.simpleboard.repository;

import com.rootlab.simpleboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
