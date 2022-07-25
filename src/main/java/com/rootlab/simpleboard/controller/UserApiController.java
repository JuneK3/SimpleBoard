package com.rootlab.simpleboard.controller;


import com.rootlab.simpleboard.model.Board;
import com.rootlab.simpleboard.model.User;
import com.rootlab.simpleboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserApiController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	List<User> all() {
		// User model의 boards 멤버가 fetch = FetchType.EAGER로 설정된 경우
		// user 정보를 가져오는 1개의 쿼리 + n개의 board 정보를 가져오는 n개의 쿼리문
		// FetchType.LAZY로 설정하면 user정보를 가져오는 1개의 쿼리만 실행
		List<User> users = userRepository.findAll();
		log.debug("getBoards().size() 호출전");
		log.debug("getBoards().size() : {}", users.get(0).getBoards().size());
		log.debug("getBoards().size() 호출후");
		return users;
	}

	@PostMapping("/users")
	User newUser(@RequestBody User newUser) {
		return userRepository.save(newUser);
	}

	@GetMapping("/users/{id}")
	User one(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@PutMapping("/users/{id}")
	User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
		return userRepository.findById(id)
				.map(user -> {
					user.getBoards().clear();
					user.getBoards().addAll(newUser.getBoards());
					for (Board board : user.getBoards()) {
						board.setUser(user);
					}
					return userRepository.save(user);
				})
				.orElseGet(() -> {
					newUser.setId(id);
					return userRepository.save(newUser);
				});
	}

	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}
