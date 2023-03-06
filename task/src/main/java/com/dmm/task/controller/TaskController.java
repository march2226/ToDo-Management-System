package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class TaskController {
	@GetMapping("/main")
	public String task(Model model) {
		List<List<LocalDate>> month = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();
		LocalDate day;
		day = LocalDate.now();
		day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);

		DayOfWeek w = day.getDayOfWeek();
		day = day.minusDays(w.getValue());

		for (int i = 1; i <= 7; i++) {
			day = day.plusDays(1);
			week.add(day);
		}

		month.add(week);
		week = new ArrayList<>();
		for (int i = 7; i <= day.lengthOfMonth(); i++) {
			System.out.println(i);
			System.out.println(w.getValue());
			System.out.println(day.lengthOfMonth());
		}

		model.addAttribute("matrix", month);
		return null;
	}

}
