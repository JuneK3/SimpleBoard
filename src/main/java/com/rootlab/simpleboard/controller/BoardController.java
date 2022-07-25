package com.rootlab.simpleboard.controller;

import com.rootlab.simpleboard.model.Board;
import com.rootlab.simpleboard.repository.BoardRepository;
import com.rootlab.simpleboard.service.BoardService;
import com.rootlab.simpleboard.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private BoardValidator boardValidator;

	@GetMapping("/list")
	public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
					   @RequestParam(required = false, defaultValue = "") String searchText) {
//		List<Board> boards = boardRepository.findAll();
//		Page<Board> boards = boardRepository.findAll(PageRequest.of(1, 20));
//		Page<Board> boards = boardRepository.findAll(pageable);
		Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
		int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
		int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("boards", boards);
		return "board/list";
	}

	@GetMapping("/form")
	public String form(Model model, @RequestParam(required = false) Long id) {
		if (id == null) {
			model.addAttribute("board", new Board());
		} else {
			Board board = boardRepository.findById(id).orElse(null);
			model.addAttribute("board", board);
		}
		return "board/form";
	}

	@PostMapping("/form")
	public String formSubmit(@Valid Board board, BindingResult bindingResult, Authentication auth) {
		boardValidator.validate(board, bindingResult);
		if (bindingResult.hasErrors()) {
			return "board/form";
		}
		String username = auth.getName();
		boardService.save(username, board);
//		boardRepository.save(board);
		return "redirect:/board/list";
	}
}
