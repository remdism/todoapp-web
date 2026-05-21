package com.example.todoapp;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CalendarService {
    boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 != 0) return false;
        return year % 4 == 0;
    }

    int computeWeekDaySub(int month, int day) {
        int[] daysBeforeMonth = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int totalDays = daysBeforeMonth[month - 1] + day;
        return (totalDays - 1) % 7;
    }

    public int computeWeekDay(int year, int month, int day) {
        int wd = (year + (year - 1) / 4 - (year - 1) / 100 + (year - 1) / 400 - 2359) % 7;
        if (wd < 0) wd += 7; 
        int offset = computeWeekDaySub(month, day);
        if (isLeapYear(year) && month >= 3) {
            offset += 1;
        }
        return (wd + offset) % 7;
    }

    // カレンダー描画用
    public List<Integer> getCalendarDays(int year, int month) {
        List<Integer> days = new ArrayList<>();
        int firstDayOfWeek = computeWeekDay(year, month, 1); // 月初の曜日
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth(); // その月の日数

        // 1日より前の空白マスを0で埋める
        for (int i = 0; i < firstDayOfWeek; i++) {
            days.add(0);
        }
        // 1日から月末までの数字を追加
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }
        // 余ったマスを42マスになるまで0で埋める
        while (days.size() < 42) {
            days.add(0);
        }
        return days;
    }
}