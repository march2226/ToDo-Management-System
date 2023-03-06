package com.dmm.task.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TaskController {

	    @GetMapping(value = "/main")
	    public String task() {
	        	List<List<LocalDate>> month = new ArrayList<>();
	        	List<LocalDate> week = new ArrayList<>();
	        	LocalDate day;
	        	day = LocalDate.now();
	        	day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);
	        	
	        	DayOfWeek w = day.getDayOfWeek();
	        	day = day.minusDays(w.getValue());
	        	
	        	for(int i = 1; i <= 7; i++) {
	        	    day = day.plusDays(1);
	        	    week.add(day);
	        	}
	        	month.add(week);
	        	week = new ArrayList<>();
	        	for(int i = 7; i <= day.lengthOfMonth(); i++) {
	        		System.out.printlin(i);
	        	}
	        	model.addAttribute("matrix", month);

		@Autowired
		private TasksRepository repo;

		/**
		 * 投稿の一覧表示.
		 * 
		 * @param model モデル
		 * @return 遷移先
		 */
		@GetMapping("/main")
		public String task(Model model) {
			// 逆順で投稿をすべて取得する
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
//    Collections.reverse(list); //普通に取得してこちらの処理でもOK
			model.addAttribute("main", list);
			TaskForm postForm = new TaskForm();
			model.addAttribute("postForm", postForm);
			return "/main";
		}

		/**
		 * 投稿を作成.
		 * 
		 * @param postForm 送信データ
		 * @param user     ユーザー情報
		 * @return 遷移先
		 */
		@PostMapping("/tasks/create")
		public String create(@Validated TaskForm taskForm, BindingResult bindingResult,
				@AuthenticationPrincipal AccountUserDetails user, Model model) {
			// バリデーションの結果、エラーがあるかどうかチェック
			if (bindingResult.hasErrors()) {
				// エラーがある場合は投稿登録画面を返す
				List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
				model.addAttribute("tasks", list);
				model.addAttribute("taskForm", taskForm);
				return "/main";
			}

			Tasks task = new Tasks();
			task.setName(user.getName());
			task.setTitle(taskForm.getTitle());
			task.setText(taskForm.getText());
			task.setDate(LocalDateTime.now());

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