package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;

@Controller

public class TaskController {
	@GetMapping("/main")
	public String task(Model model) {
		MultiValueMap<LocalDate, Tasks> Tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		List<List<LocalDate>> month = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();
		LocalDate day;
		day = LocalDate.now();
		day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);

		DayOfWeek w = day.getDayOfWeek();
		day = day.minusDays(w.getValue());

		for (int i = 1; i <= 7; i++) {
			week.add(day);
			day = day.plusDays(1);

			w = day.getDayOfWeek();

			System.out.println(i);

		}
		month.add(week);
		week = new ArrayList<>();

		for (int i = 7; i <= day.lengthOfMonth(); i++) {
			week.add(day);

			w = day.getDayOfWeek(); // 1日進めた曜日を取得
			if (w == DayOfWeek.SATURDAY) {
				month.add(week);

				week = new ArrayList<>();

				System.out.println(i);
				System.out.println(w.getValue());
				System.out.println(day.lengthOfMonth());
			}
			day = day.plusDays(1);
		}
		w = day.getDayOfWeek();
		int nextMonthDays = 7 - w.getValue();
		for (int i = 1; i <= nextMonthDays; i++) {
			week.add(day);
			day = day.plusDays(1);
			System.out.println(i);
			System.out.println(w.getValue());
		}
		month.add(week);
		model.addAttribute("tasks", Tasks);
		model.addAttribute("matrix", month);

		return "main";

	}

}
