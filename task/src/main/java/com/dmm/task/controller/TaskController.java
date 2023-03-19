package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	public String task(Model model, @AuthenticationPrincipal AccountUserDetails user,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		List<List<LocalDate>> month = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();
		LocalDate day;
		LocalDate start;
		LocalDate end;
		String name = user.getName();
		if (date == null) {
			// dateが渡ってこなかった場合＝今月
			day = LocalDate.now();
			day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);
		} else {
			// dateが渡ってきた場合＝前月or翌月。渡ってきたdateをそのまま使う
			day = date;
		}
		model.addAttribute("prev", day.minusMonths(1));
		model.addAttribute("next", day.plusMonths(1));

		day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);
		start = day;

		System.out.println(day);

		DayOfWeek w = day.getDayOfWeek();
		day = day.minusDays(w.getValue());

		for (int i = 1; i <= 7; i++) {
			week.add(day);
			day = day.plusDays(1);

			w = day.getDayOfWeek();

		}
		month.add(week);
		week = new ArrayList<>();

		for (int i = 7; i <= day.lengthOfMonth(); i++) {
			week.add(day);

			w = day.getDayOfWeek(); // 1日進めた曜日を取得
			if (w == DayOfWeek.SATURDAY) {
				month.add(week);

				week = new ArrayList<>();

			}
			day = day.plusDays(1);
		}

		end = day;
		List<Tasks> list;
		System.out.println(name);
		if(name=="user") {
			list = repo.findByDateBetween(start.atTime(0, 0), end.atTime(0, 0), name);
		}else {
			list = repo.findAllByDateBetween(start.atTime(0, 0), end.atTime(0, 0));
		}
		
		for (Tasks t : list) {

			LocalDate d = t.getDate().toLocalDate();

			tasks.add(d, t);
		}

		System.out.println(day);
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
