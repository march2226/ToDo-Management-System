package com.dmm.task.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;

public class MainController {
	@Controller
	@RequestMapping(value = "topPage")
	public class TopPageController {

		@Autowired
		private TasksRepository repo;

		@RequestMapping(value = "getPage", method = RequestMethod.GET)
		public String getPage(TaskForm form, Model model) {

			Calendar rightNow = Calendar.getInstance();
			@SuppressWarnings("unused")
			int day = rightNow.get(Calendar.DATE);
			int year = rightNow.get(Calendar.YEAR);
			int month = rightNow.get(Calendar.MONTH);

	        // 今月のはじまり 
			rightNow.set(year, month, 1);
			int startWeek = rightNow.get(Calendar.DAY_OF_WEEK);

	        // 先月分の日数 
			rightNow.set(year, month, 0);
			int beforeMonthlastDay = rightNow.get(Calendar.DATE);

			// 今月分の日数 
			rightNow.set(year, month + 1, 0);
			int thisMonthlastDay = rightNow.get(Calendar.DATE);

			int[] calendarDay = new int[42];		// 最大で7日×6週
			int count = 0;

			for (int i = startWeek - 2; i >= 0; i--) {
				calendarDay[count++] = beforeMonthlastDay - i;
			}

			for (int i = 1; i <= thisMonthlastDay; i++) {
				calendarDay[count++] = i;
			}

			int nextMonthDay = 1;
			while (count % 7 != 0) {
				calendarDay[count++] = nextMonthDay++;
			}

			int weekCount = count / 7;

			for (int i = 0; i < weekCount; i++) {
				for (int j = i * 7; j < i * 7 + 7; j++) {
					if (calendarDay[j] < 10) {
						System.out.print(" " + calendarDay[j] + " ");
					} else {
						System.out.print(calendarDay[j] + " ");

					}
				}
				System.out.println();
			}

			
			return "mypage";
		}
	}
}