package com.android.showmeeapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.pojo.CalendarData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by anandbose on 09/06/15.
 */
public class CalendarViewAdapter extends Adapter<CalendarViewAdapter.ViewHolder> {
	private static final String tag = "Adapter";
	private final ArrayList<String> list = new ArrayList<>();
	private Context mContext;
	private int daysInMonth;
	private View.OnClickListener onClickListener;


	public CalendarViewAdapter(Context context, String date, String month, String year, View.OnClickListener onclickListener) {
		this.mContext = context;
		this.onClickListener = onclickListener;
		//Log.e("", "In Constructor Month: " + month + " " + "Year: " + year + " " + "Date: " + date);
		setCurrentDate(date, month, year);
	}

	@Override
	public CalendarViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = null;
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar_date_itme, parent, false);
		ViewHolder vhItem = new ViewHolder(v);
		return vhItem;
	}

	@Override
	public void onBindViewHolder(final CalendarViewAdapter.ViewHolder holder, int position) {
		String[] day_color = list.get(position).split("-");
		String day_current = day_color[0];
		String month_current = day_color[2];
		String year_current = day_color[3];
		String month = day_color[4];

		// Set the Day GridCell
		holder.txtVwDate.setOnClickListener(onClickListener);
		holder.txtVwDate.setText(day_current);
		holder.txtVwDate.setTag(day_current + "-" + month_current + "-" + year_current + "-" + month);

		//Log.d(tag, "Setting GridCell " + day_current + "-" + month_current + "-" + year_current + "-" + month);


		// day of last or previus month
		if (day_color[1].equals("NOT_CURRENT_MONTH_DAY")) {
			holder.txtVwDate.setTextColor(mContext.getResources().getColor(R.color.bg_light_grey));
		}
		// day of current month
		if (day_color[1].equals("CURRENT_MONTH_DAY")) {
			holder.txtVwDate.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		// current day of month
		if (day_color[1].equals("CURRENT_DAY")) {
			holder.txtVwDate.setBackgroundColor(mContext.getResources().getColor(R.color.theme_color));
			holder.txtVwDate.setTextColor(mContext.getResources().getColor(R.color.white));
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public void setCurrentDate(String date, String month, String year) {
		Calendar calendar = Calendar.getInstance();
		String dateFormate = year + "-" + month + "-" + date;

		if (date != "" && month != "" && year != "") {
			CalendarData.setCurrentDayOfMonth(Integer.parseInt(date));  // Set Current Date
			CalendarData.setCurrentWeekDay(getDayofWeek(dateFormate));  // Set Current Day
		} else {
			CalendarData.setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			CalendarData.setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
		}

		//Log.e(tag, "CurrentDayOfWeek :" + CalendarData.getCurrentWeekDay());
		//Log.e(tag, "CurrentDayOfMonth :" + CalendarData.getCurrentDayOfMonth());

		printMonth(Integer.parseInt(month), Integer.parseInt(year));
	}

	private void printMonth(int month, int year) {
		int trailingSpaces = 0;
		int daysInPrevMonth = 0;
		int prevMonth = 0;
		int prevYear = 0;
		int nextMonth = 0;
		int nextYear = 0;
		int currentMonth = month - 1;
		//Log.e(tag, "currentMonth ofter -1 :" + currentMonth);
		String currentMonthName = CalendarData.getMonthAsString(currentMonth);
		daysInMonth = CalendarData.getNumberOfDaysOfMonth(currentMonth);

		//Log.e(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");


		GregorianCalendar cal = new GregorianCalendar(year, currentMonth, 1);
		//Log.e(tag, "Gregorian Calendar:= " + cal.getTime().toString());

		if (currentMonth == 11) {
			prevMonth = currentMonth - 1;
			daysInPrevMonth = CalendarData.getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 0;
			prevYear = year;
			nextYear = year + 1;
			//Log.e(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
		} else if (currentMonth == 0) {
			prevMonth = 11;
			prevYear = year - 1;
			nextYear = year;
			daysInPrevMonth = CalendarData.getNumberOfDaysOfMonth(prevMonth);
			nextMonth = 1;
			//Log.e(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
		} else {
			prevMonth = currentMonth - 1;
			nextMonth = currentMonth + 1;
			nextYear = year;
			prevYear = year;
			daysInPrevMonth = CalendarData.getNumberOfDaysOfMonth(prevMonth);
			//Log.e(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
		}

		//int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		trailingSpaces = CalendarData.getCurrentWeekDay() - 1;

		//Log.e(tag, "Week Day:" + trailingSpaces + " is " + CalendarData.getWeekDayAsString(trailingSpaces));
		//Log.e(tag, "No. Trailing space to Add: " + trailingSpaces);
		//Log.e(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

		//if (cal.isLeapYear(cal.get(Calendar.YEAR)))
		if (cal.isLeapYear(year))
			if (month == 2)
				++daysInMonth;
			else if (month == 3)
				++daysInPrevMonth;

		CalendarData.setCurrentMonth(currentMonth);
		CalendarData.setCurrentYear(year);
		CalendarData.setPrevMonth(prevMonth);
		CalendarData.setNextMonth(nextMonth);
		CalendarData.setPrevYear(prevYear);
		CalendarData.setNextYear(nextYear);

		int current_date = CalendarData.getCurrentDayOfMonth();
		int current_week_no = CalendarData.getCurrentWeekDay();
		int next_months_dates = 1;


		while (current_week_no > 1) {
			if (current_date > 1) {
				current_date--;  // current date- 1
				trailingSpaces--;
				//Log.d(tag, "currrent date** : " + current_date);
			}
			current_week_no--;
		}
		//	Log.e(tag, "final currrent date is : " + current_date);
		//	Log.e(tag, "trailingSpaces  is : " + trailingSpaces + " && " + ((daysInPrevMonth - trailingSpaces)+1));
		// Trailing Month days
		for (int i = 0; i < trailingSpaces && current_date == 1; i++) {
		/*	Log.d(tag,
					"PREV MONTH:= "
							+ prevMonth
							+ " => "
							+ CalendarData.getMonthAsString(prevMonth)
							+ " "
							+ String.valueOf((daysInPrevMonth - trailingSpaces)+1)
							+ i);*/
			list.add(String.valueOf(((daysInPrevMonth - trailingSpaces) + 1) + i)
					+ "-GREY"
					+ "-"
					+ CalendarData.getMonthAsString(prevMonth)
					+ "-"
					+ prevYear
					+ "-"
					+ prevMonth);
		}
		int listSize = list.size();
		for (int i = 0; current_date <= daysInMonth + 14 && i < 14 - listSize; current_date++, i++) {
			//	Log.d(currentMonthName, String.valueOf(current_date) + " " + CalendarData.getMonthAsString(currentMonth) + " " + year);

			if (current_date <= daysInMonth) {
				if (current_date == CalendarData.getCurrentDayOfMonth()) {
					list.add(String.valueOf(current_date) + "-CURRENT_DAY" + "-" + CalendarData.getMonthAsString(currentMonth) + "-" + year + "-" + currentMonth);
				} else {
					list.add(String.valueOf(current_date) + "-CURRENT_MONTH_DAY" + "-" + CalendarData.getMonthAsString(currentMonth) + "-" + year + "-" + currentMonth);
				}
			} else {
				Log.e(tag, "NEXT MONTH:= " + CalendarData.getMonthAsString(nextMonth));
				list.add(String.valueOf(next_months_dates) + "-NOT_CURRENT_MONTH_DAY" + "-" + CalendarData.getMonthAsString(nextMonth) + "-" + nextYear + "-" + nextMonth);
				next_months_dates++;
			}

		}
	}

	public int getDayofWeek(String dateFormate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
		Date date = null;
		Calendar c = null;
		try {
			date = dateFormat.parse(dateFormate); //"2014-09-10 13:45:20"
			//System.out.println("date after formate : " + date);
			c = Calendar.getInstance();
			c.setTime(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView txtVwDate;

		public ViewHolder(View itemView) {
			super(itemView);
			txtVwDate = (TextView) itemView.findViewById(R.id.txtDateOfWeek);
		}
	}
}
		/*// Current Month Days
		for (int k = 1; k <= daysInMonth; k++) {
			Log.d(currentMonthName, String.valueOf(k) + " " + getMonthAsString(currentMonth) + " " + year);
			if (k == getCurrentDayOfMonth()) {
				list.add(String.valueOf(k) + "-CURRENT_DAY" + "-" + getMonthAsString(currentMonth) + "-" + year);
			} else {
				list.add(String.valueOf(k) + "-CURRENT_MONTH_DAY" + "-" + getMonthAsString(currentMonth) + "-" + year);
			}
		}
		// Leading Month days
		for (int k = 0; k < list.size() % 7; k++) {
			Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
			list.add(String.valueOf(k + 1) + "-NOT_CURRENT_MONTH_DAY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
		}*/
