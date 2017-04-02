package com.android.showmeeapp.model;

/**
 * Created by kushahuja on 8/8/16.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateEvent {

	@SerializedName("summary")
	@Expose
	private String summary;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("start")
	@Expose
	private Start start;
	@SerializedName("end")
	@Expose
	private End end;
	@SerializedName("location")
	@Expose
	private String location;
	@SerializedName("visibility")
	@Expose
	private String visibility;
	@SerializedName("tags")
	@Expose
	private String tags;
	/**
	 * @return The summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary The summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The start
	 */
	public Start getStart() {
		return start;
	}

	/**
	 * @param start The start
	 */
	public void setStart(Start start) {
		this.start = start;
	}

	/**
	 * @return The end
	 */
	public End getEnd() {
		return end;
	}

	/**
	 * @param end The end
	 */
	public void setEnd(End end) {
		this.end = end;
	}

	/**
	 * @return The location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location The location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return The visibility
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility The visibility
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}


}

