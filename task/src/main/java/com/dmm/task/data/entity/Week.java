package com.dmm.task.data.entity;

import java.util.ArrayList;
import java.util.List;


public class Week {
    private List<Integer> weekdays = new ArrayList<>();

    public List<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Integer> weekdays) {
        this.weekdays = weekdays;
    }
}