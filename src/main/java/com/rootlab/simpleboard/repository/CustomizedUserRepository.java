package com.rootlab.simpleboard.repository;

import com.rootlab.simpleboard.model.User;
import java.util.List;

public interface CustomizedUserRepository {
	List<User> findByUsernameCustom(String username);

	List<User> findByUsernameJDBC(String username);
}
