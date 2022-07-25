package com.rootlab.simpleboard.service;

import com.rootlab.simpleboard.model.Role;
import com.rootlab.simpleboard.model.User;
import com.rootlab.simpleboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User save(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		Role role = new Role();
		role.setId(1l);
		user.getRoles().add(role);
		return userRepository.save(user);
	}
}
