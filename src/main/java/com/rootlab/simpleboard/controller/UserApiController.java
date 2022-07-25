package com.rootlab.simpleboard.controller;


import com.rootlab.simpleboard.model.Board;
import com.rootlab.simpleboard.model.User;
import com.rootlab.simpleboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	List<User> all() {
		return userRepository.findAll();
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
					for(Board board : user.getBoards()) {
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
