package com.rootlab.simpleboard.service;

import com.rootlab.simpleboard.model.Board;
import com.rootlab.simpleboard.model.User;
import com.rootlab.simpleboard.repository.BoardRepository;
import com.rootlab.simpleboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private UserRepository userRepository;

	public Board save(String username, Board board) {
		User user = userRepository.findByUsername(username);
		board.setUser(user);
		return boardRepository.save(board);
	}
}
