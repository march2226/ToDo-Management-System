package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.service.AccountUserDetails;

@Controller

public class TaskController {

	@Autowired
	private TasksRepository repo;

	@GetMapping("/main")
	public String task(Model model,@AuthenticationPrincipal AccountUserDetails user) {
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		String name = user.getName();
		LocalDate start;
		LocalDate end;
		List<List<LocalDate>> month = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();
		LocalDate day;
		day = LocalDate.now();
		day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);
		start = day;
		System.out.println(day);

		DayOfWeek w = day.getDayOfWeek();
		day = day.minusDays(w.getValue());
		end = day;
		
		System.out.println(day);

		List<Tasks> list;
		user.getName();
		list = repo.findByDateBetween(start,end,user.getName().toLocalDate());
		list = repo.findAllByDateBetween(start.toLocalDate(),end.toLocalDate());
		for (Tasks t : list) {
			
			LocalDate date = t.getDate().toLocalDate();
			
			tasks.add(date,t);
		}

		for (int i = 1; i <= 7; i++) {
			week.add(day);
			day = day.plusDays(1);

			w = day.getDayOfWeek();
			
			model.addAttribute("prev", day.minusMonths(1));
			model.addAttribute("next", day.plusMonths(1));

			System.out.println(day);

		}
		month.add(week);
		week = new ArrayList<>();

		for (int i = 7; i <= day.lengthOfMonth(); i++) {
			week.add(day);

			w = day.getDayOfWeek(); // 1日進めた曜日を取得
			if (w == DayOfWeek.SATURDAY) {
				month.add(week);

				week = new ArrayList<>();

				System.out.println(day);
			}
			day = day.plusDays(1);
		}
		w = day.getDayOfWeek();
		int nextMonthDays = 7 - w.getValue();
		for (int i = 1; i <= nextMonthDays; i++) {
			week.add(day);
			day = day.plusDays(1);
			
			}
		month.add(week);
		model.addAttribute("tasks", tasks);
		model.addAttribute("matrix", month);

		return "main";

	}

}
