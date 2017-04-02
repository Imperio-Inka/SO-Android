package com.android.showmeeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kushahuja on 8/8/16.
 */
public class EventTags {
	@SerializedName("tags")
	@Expose
	private String[] tags;

	/**
	 * @return The Tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @param tags The Tags
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

}
