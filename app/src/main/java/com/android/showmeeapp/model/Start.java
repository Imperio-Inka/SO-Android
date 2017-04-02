package com.android.showmeeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Start {

	@SerializedName("dateTime")
	@Expose
	private String dateTime;

	/**
	 * @return The dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime The dateTime
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
