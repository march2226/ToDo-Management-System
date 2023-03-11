package com.dmm.task.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.PostForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class PostController {

	@Autowired
	private TasksRepository repo;

	/**
	 * 投稿の一覧表示.
	 * 
	 * @param model モデル
	 * @return 遷移先
	 */
	@GetMapping("/main/create/{date}")
	public String posts(Model model, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		model.addAttribute("date", date);
	   		return "/create";
	}

	/**
	 * 投稿を作成.
	 * 
	 * @param postForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	@PostMapping("/main/create")
	public String create(@Validated PostForm postForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {
		// バリデーションの結果、エラーがあるかどうかチェック
		if (bindingResult.hasErrors()) {
			// エラーがある場合は投稿登録画面を返す
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("tasks", list);
			model.addAttribute("postForm", postForm);
			return "/main";
		}

		Tasks task = new Tasks();
		task.setName(user.getName());
		task.setTitle(postForm.getTitle());
		task.setText(postForm.getText());
		task.setDate(LocalDateTime.now());

		repo.save(task);

		return "redirect:/main";
	}

	@GetMapping("/main/edit/{id}")
	public String getById(Model model, @PathVariable Integer id) {
		Tasks task = repo.getById(id);
		model.addAttribute("task", task);
		return "/edit";

	}
	@PostMapping("/main/edit/{id}")
	public String edit(@Validated PostForm postForm, BindingResult bindingResult,
			Model model,@PathVariable Integer id) {
		
		Tasks task = repo.getById(id);
		model.addAttribute("task", task);
		task.setName(task.getName());
		task.setTitle(postForm.getTitle());
		task.setText(postForm.getText());
		task.setDate(LocalDateTime.now());
		task.setDone(postForm.isDone());
		repo.save(task);
		return "redirect:/main";
	}

	/**
	 * 投稿を削除する
	 * 
	 * @param id 投稿ID
	 * @return 遷移先
	 */
	@PostMapping("/main/delete/{id}")
	public String delete(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/main";
	}
}