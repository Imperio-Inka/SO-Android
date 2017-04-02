package com.android.showmeeapp.pojo;

import com.android.showmeeapp.constant.Constant;

/**
 * Created by yuva on 18/7/16.
 */
public class CalendarData {
	public static int currentDay;
	public static int currentYear;
	public static int currentMonth;
	public static int currentWeekDay;
	public static int prevMonth;
	public static int nextMonth;
	public static int prevYear;
	public static int nextYear;
	public static String todayCompleteDate;
	public static String tommarowCompleteDate;

	public static int getCurrentDayOfMonth() {
		return currentDay;
	}

	public static void setCurrentDayOfMonth(int currentDayOfMonth) {
		CalendarData.currentDay = currentDayOfMonth;
	}

	public static int getCurrentWeekDay() {
		return currentWeekDay;
	}

	public static void setCurrentWeekDay(int currentWeekDay) {
		CalendarData.currentWeekDay = currentWeekDay;
	}

	public static int getCurrentYear() {
		return currentYear;
	}

	public static void setCurrentYear(int currentYear) {
		CalendarData.currentYear = currentYear;
	}

	public static String getMonthAsString(int i) {
		return Constant.months[i];
	}

	public static String getWeekDayAsString(int i) {
		return Constant.weekdays[i];
	}

	public static int getNumberOfDaysOfMonth(int i) {
		return Constant.daysOfMonth[i];
	}

	public static int getPrevMonth() {
		return prevMonth;
	}

	public static void setPrevMonth(int prevMonth) {
		CalendarData.prevMonth = prevMonth;
	}

	public static int getNextMonth() {
		return nextMonth;
	}

	public static void setNextMonth(int nextMonth) {
		CalendarData.nextMonth = nextMonth;
	}

	public static int getPrevYear() {
		return prevYear;
	}

	public static void setPrevYear(int prevYear) {
		CalendarData.prevYear = prevYear;
	}

	public static int getNextYear() {
		return nextYear;
	}

	public static void setNextYear(int nextYear) {
		CalendarData.nextYear = nextYear;
	}

	public static int getCurrentMonth() {
		return currentMonth;
	}

	public static void setCurrentMonth(int currentMonth) {
		CalendarData.currentMonth = currentMonth;
	}

	public static String getTodayCompleteDate() {
		return todayCompleteDate;
	}

	public static void setTodayCompleteDate(String todayCompleteDate) {
		CalendarData.todayCompleteDate = todayCompleteDate;
	}

	public static String getTommarowCompleteDate() {
		return tommarowCompleteDate;
	}

	public static void setTommarowCompleteDate(String tommarowCompleteDate) {
		CalendarData.tommarowCompleteDate = tommarowCompleteDate;
	}
}
