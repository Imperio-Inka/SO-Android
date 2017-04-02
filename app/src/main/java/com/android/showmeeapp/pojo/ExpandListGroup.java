package com.android.showmeeapp.pojo;

import java.util.ArrayList;

/**
 * Created by yuva on 18/7/16.
 */
public class ExpandListGroup {

	public String groupName;
	public ArrayList<CalendarData> tagList = new ArrayList<CalendarData>();

	public ExpandListGroup(String groupName, ArrayList<CalendarData> tagList) {
		this.groupName = groupName;
		this.tagList = tagList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<CalendarData> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<CalendarData> tagList) {
		this.tagList = tagList;
	}
}
