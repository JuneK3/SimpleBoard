package com.rootlab.simpleboard.controller;


import com.querydsl.core.types.Predicate;
import com.rootlab.simpleboard.model.Board;
import com.rootlab.simpleboard.model.QUser;
import com.rootlab.simpleboard.model.User;
import com.rootlab.simpleboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserApiController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	Iterable<User> all(@RequestParam(required = false) String method,
					   @RequestParam(required = false) String text) {
		// User model의 boards 멤버가 fetch = FetchType.EAGER로 설정된 경우
		// user 정보를 가져오는 1개의 쿼리 + n개의 board 정보를 가져오는 n개의 쿼리문
		// FetchType.LAZY로 설정하면 user정보를 가져오는 1개의 쿼리만 실행
//		List<User> users = userRepository.findAll();
//		log.debug("getBoards().size() 호출전");
//		log.debug("getBoards().size() : {}", users.get(0).getBoards().size());
//		log.debug("getBoards().size() 호출후");
//		return users;

		Iterable<User> users = null;
		if ("query".equals(method)) {
			log.debug("query");
			users = userRepository.findByUsernameQuery(text);
		} else if ("nativeQuery".equals(method)) {
			log.debug("native query");
			users = userRepository.findByUsernameNativeQuery(text);
		} else if ("querydsl".equals(method)) {
			log.debug("querydsl");
			QUser user = QUser.user;
			Predicate predicate = user.username.contains(text);
			users = userRepository.findAll(predicate);
		} else if ("querydslCustom".equals(method)) {
			users = userRepository.findByUsernameCustom(text);
		} else if ("jdbc".equals(method)) {
			users = userRepository.findByUsernameJDBC(text);
		} else {
			users = userRepository.findAll();
		}
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
